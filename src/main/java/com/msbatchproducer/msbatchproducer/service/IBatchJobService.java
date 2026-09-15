package com.msbatchproducer.msbatchproducer.service;

import org.springframework.batch.core.JobExecution;

public interface IBatchJobService {

    JobExecution launch(String filePath);

}
