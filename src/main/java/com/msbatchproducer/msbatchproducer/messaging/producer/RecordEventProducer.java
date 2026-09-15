package com.msbatchproducer.msbatchproducer.messaging.producer;

import com.msbatchproducer.msbatchproducer.model.dto.RecordReadyEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecordEventProducer {

    private final KafkaTemplate<String, RecordReadyEvent> kafkaTemplate;

    @Value("${app.kafka.topic.record-ready}")
    private String recordReadyTopic;

    public void publish(RecordReadyEvent event) {
        String key = event.getRecordId() != null ? event.getRecordId().toString() : null;
        kafkaTemplate.send(recordReadyTopic, key, event);
    }

}
