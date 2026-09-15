package com.msbatchproducer.msbatchproducer.service.impl;

import com.msbatchproducer.msbatchproducer.service.IBatchJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BatchJobServiceImpl implements IBatchJobService {

    private final JobLauncher jobLauncher;
    private final Job inputFileJob;

    @Override
    public JobExecution launch(String filePath) {
        try{
            JobParameters params = new JobParametersBuilder()
                    .addString("filePath", filePath)
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();

            return jobLauncher.run(inputFileJob, params);
        }catch(Exception e){
            throw new RuntimeException("ERROR LAUNCHING QQ: ",e);
        }
    }

}
