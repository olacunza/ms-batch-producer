package com.msbatchproducer.msbatchproducer.batch.writer;

import com.msbatchproducer.msbatchproducer.messaging.producer.RecordEventProducer;
import com.msbatchproducer.msbatchproducer.model.dto.RecordReadyEvent;
import com.msbatchproducer.msbatchproducer.model.entity.BatchRecord;
import com.msbatchproducer.msbatchproducer.repository.IBatchRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InputRecordWriter implements ItemWriter<BatchRecord> {

    private final IBatchRecordRepository batchRecordRepository;
    private final RecordEventProducer recordEventProducer;

    @Override
    public void write(Chunk<? extends BatchRecord> chunk) {
        List<BatchRecord> records = new ArrayList<>(chunk.getItems());
        if(records.isEmpty()) { return; }

        List<BatchRecord> savedRecords = batchRecordRepository.saveAll(records);

        for (BatchRecord record : savedRecords) {
            RecordReadyEvent event = new RecordReadyEvent();
            event.setRecordId(record.getId());
            event.setBusinessKey(record.getBusinessKey());
            recordEventProducer.publish(event);
        }
    }

}
