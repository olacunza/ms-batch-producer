package com.msbatchproducer.msbatchproducer.controller;

import com.msbatchproducer.msbatchproducer.model.dto.BatchLaunchRequest;
import com.msbatchproducer.msbatchproducer.model.dto.BatchLaunchResponse;
import com.msbatchproducer.msbatchproducer.service.IBatchJobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobExecution;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/batch")
@RequiredArgsConstructor
public class BatchJobController {

    private final IBatchJobService batchJobService;

    @PostMapping("/launch")
    public ResponseEntity<BatchLaunchResponse> launch(
            @Valid @RequestBody BatchLaunchRequest request ){

        JobExecution execution = batchJobService.launch(request.getFilePath());
        BatchLaunchResponse response = new BatchLaunchResponse();
        response.setJobExecutionId(execution.getId());

        return ResponseEntity.accepted().body(response);
    }

}
