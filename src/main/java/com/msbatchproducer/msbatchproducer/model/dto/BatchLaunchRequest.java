package com.msbatchproducer.msbatchproducer.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BatchLaunchRequest {

    @NotBlank
    private String filePath;

}
