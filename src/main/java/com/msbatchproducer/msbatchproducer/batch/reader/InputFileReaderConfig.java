package com.msbatchproducer.msbatchproducer.batch.reader;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.PassThroughLineMapper;
import org.springframework.batch.item.file.separator.SimpleRecordSeparatorPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class InputFileReaderConfig {

    @Bean
    @StepScope
    public FlatFileItemReader<String> inputFileReader(
            @Value("#{jobParameters['filePath']}") String filePath) {

                FlatFileItemReader<String> reader = new FlatFileItemReader<>();
                reader.setName("inputFileReader");
                reader.setResource(new FileSystemResource(filePath));
                reader.setEncoding("UTF-8");
                reader.setLinesToSkip(0);
                reader.setStrict(true);
                reader.setRecordSeparatorPolicy(new SimpleRecordSeparatorPolicy());

                LineMapper<String> lineMapper = new PassThroughLineMapper();
                reader.setLineMapper(lineMapper);

                return reader;

    }

}
