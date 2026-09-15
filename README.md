# Procesamiento de XML por eventos

Java 21
Spring Boot 3.3.5
Spring Batch
JPA/Hibernate
SQL Server 2022
Kafka 3.9.1.

## Alcance

- **A — ms-batch-producer:** recibe XML/ZIP, lee chunks, persiste registros y publica eventos mediante outbox.
- **B — ms-event-consumer:** consume lotes de Kafka, procesa XML, guarda resultados y deduplica eventos. Los mensajes irrecuperables se conservan en cuarentena SQL.

## Arranque con Docker

Organiza los proyectos como carpetas hermanas:

```text
reto-eventos/
├── ms-batch-producer/
│   ├── Dockerfile
│   └── docker-compose.yml
└── ms-event-consumer/
    └── Dockerfile
```

Requisitos: Docker Engine/Desktop con Compose v2 o superior, conexión a Internet para la primera construcción y memoria disponible para SQL Server y Java. Las aplicaciones no requieren Java ni Maven instalados en el host. 
SQL Server se ejecuta como linux/amd64; en Apple Silicon utiliza emulación, que no equivale a una plataforma de producción soportada.

Desde `ms-batch-producer`:

```sh
cp .env.example .env
# Edita MSSQL_SA_PASSWORD en .env con tu propia contraseña.
docker compose up -d --build --wait
```

Puertos predeterminados: A 8080, B 8081, SQL Server 1433 y Kafka 9092. 
Si ya están ocupados, cambia `PRODUCER_PORT`, `CONSUMER_PORT`, `SQL_PORT` y `KAFKA_PORT` en `.env`. 
No es necesario borrar los contenedores del entorno anterior.

Compose crea un entorno independiente llamado `reto-eventos`: SQL Server, base `reto_db`, broker Kafka, tópico, A y B. 
Espera la salud de cada dependencia.
Las imágenes de aplicación se construyen en dos etapas: Maven ejecuta `verify`, luego el JAR se copia a Java 21 JRE y se ejecuta como usuario sin privilegios.
Las credenciales se pasan en ejecución y quedan fuera del contexto de construcción.

```sh
docker compose ps
curl http://localhost:8080/actuator/health
curl http://localhost:8081/actuator/health
```

## Enviar un archivo

En Postman importa `postman/servicio-a.postman_collection.json` y la colección de B.
Usa **POST** `http://localhost:8080/api/batch/launch`, **Body → form-data**, campo **file**, tipo **File**, seleccionando XML o ZIP.
Deja que Postman genere el Content-Type multipart.

La respuesta inicial es 202. `COMPLETED` describe la ingestión de A, `pendingEvents=0` describe la publicación a Kafka
y cero registros PENDING describe el fin del procesamiento secundario de esa carga.

Un XML inválido queda FAILED desde A y no genera evento.

El ZIP puede contener carpetas y metadatos de macOS.
Se omiten `__MACOSX/`, `.DS_Store`, `._*` y directorios.
Se conserva el XML original, incluidos rechazos; no se extraen rutas ZIP.

Límites: subida 25 MiB, XML 1 MiB, 100 MiB descomprimidos, 10000 XML y 30000 entradas totales.
Los metadatos también respetan límites de bytes.

`stop`, `start` y `down` sin `--volumes` conservan los volúmenes. No se necesita limpiar Kafka después de una carga: conserva mensajes por retención y cada grupo recuerda su avance.

## Publicar código e imágenes

Cada proyecto incluye Dockerfile, fuentes, pruebas y documentación; excluye `.env`, `config/sqlserver-local.properties`, `.idea`, `target` y datos subidos.
Puedes publicar ambos en un monorepo conservando las carpetas hermanas o clonarlos juntos desde repositorios separados.

`IMAGE_PREFIX` define el namespace del registro; `IMAGE_TAG` la versión.
Ejemplo en `.env`: `IMAGE_PREFIX=ghcr.io/tu-usuario`, `IMAGE_TAG=1.0.0`.

Después de publicar las imágenes, en otra máquina con el repositorio y su `.env`:

```sh
docker compose pull
docker compose up -d --no-build --wait
```

Para publicar ARM64 y AMD64 desde un builder compatible, usar `docker buildx build --platform linux/amd64,linux/arm64 --push -t REGISTRO/IMAGEN:VERSION .`en cada servicio.
El build normal genera la arquitectura del host.
Las imágenes de Kafka y SQL Server provienen de sus proyectos oficiales; no necesitan Dockerfile personalizado.

