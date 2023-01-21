package com.ngyewkong.springbatchdatabasemigration;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableAsync
@EnableBatchProcessing
@ComponentScan("com.ngyewkong")
@SpringBootApplication
public class SpringbatchDatabaseMigrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbatchDatabaseMigrationApplication.class, args);
    }

}
