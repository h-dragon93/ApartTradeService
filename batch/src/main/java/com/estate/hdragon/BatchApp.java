package com.estate.hdragon;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;


@EnableBatchProcessing
@SpringBootApplication
public class BatchApp {

    public static void main(String[] args) {
        SpringApplication.run(BatchApp.class, args);
    }

}


