package com.msbatchproducer.msbatchproducer.model.dto;

import lombok.Data;

@Data
public class BatchLaunchResponse {

    private Long jobExecutionId;
    private String status;

}
