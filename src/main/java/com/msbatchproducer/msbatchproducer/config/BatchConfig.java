package com.msbatchproducer.msbatchproducer.config;

import com.msbatchproducer.msbatchproducer.batch.listener.JobCompletionListener;
import com.msbatchproducer.msbatchproducer.batch.processor.InputRecordProcessor;
import com.msbatchproducer.msbatchproducer.batch.writer.InputRecordWriter;
import com.msbatchproducer.msbatchproducer.model.entity.BatchRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final JobCompletionListener jobCompletionListener;

    @Value("${app.batch.chunk-size:100}")
    private int chunkSize;

    @Bean
    public Step processInputFileStep(
            FlatFileItemReader<String> inputFileReader,
            InputRecordProcessor inputRecordProcessor,
            InputRecordWriter inputRecordWriter){

        return new StepBuilder("processInputFileStep", jobRepository)
                .<String, BatchRecord>chunk(chunkSize, transactionManager)
                .reader(inputFileReader)
                .processor(inputRecordProcessor)
                .writer(inputRecordWriter)
                .build();
    }

    @Bean
    public Job inputFileJob(Step processInputFileStep) {
        return new JobBuilder("inputFileJob", jobRepository)
                .listener(jobCompletionListener)
                .start(processInputFileStep)
                .build();
    }

}
