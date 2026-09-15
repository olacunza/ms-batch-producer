package com.msbatchproducer.msbatchproducer.model.dto;

import lombok.Data;

@Data
public class RecordReadyEvent {

    private Long recordId;
    private Long businessKey;

}
