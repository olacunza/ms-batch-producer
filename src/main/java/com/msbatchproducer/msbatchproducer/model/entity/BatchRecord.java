package com.msbatchproducer.msbatchproducer.model.entity;

import com.msbatchproducer.msbatchproducer.model.enums.RecordStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.batch.core.BatchStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "batch_record")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BatchRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long businessKey;

    @Enumerated(EnumType.STRING)
    private RecordStatus status;

    @Column(nullable = false)
    private String errorMessage;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
