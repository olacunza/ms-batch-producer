package com.msbatchproducer.msbatchproducer.batch.processor;

import com.msbatchproducer.msbatchproducer.model.entity.BatchRecord;
import com.msbatchproducer.msbatchproducer.model.enums.RecordStatus;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class InputRecordProcessor implements ItemProcessor<String, BatchRecord> {

    @Override
    public BatchRecord process(String line) {
        if(line == null || line.isBlank()) { return null; }

        Long businessKey = Long.parseLong(line.trim());

        return BatchRecord.builder()
                .businessKey(businessKey)
                .status(RecordStatus.PENDING)
                .build();
    }

}
