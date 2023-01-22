package com.ngyewkong.springbatchdatabasemigration.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@Configuration
public class StudentMigrationJobConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    // autowired the EMF for both db
    // use postgresEMF in JPA Item Reader
    // use mysqlEMF in JPA Item Writer
    @Autowired
    @Qualifier("postgresqlEntityManagerFactory")
    private EntityManagerFactory postgresEMF;

    @Autowired
    @Qualifier("mysqlEntityManagerFactory")
    private EntityManagerFactory mysqlEMF;

    @Bean
    public Job studentMigrationJob() {
        return jobBuilderFactory.get("student_migration_job")
                .incrementer(new RunIdIncrementer())
                .start(studentMigrationChunkStep())
                .build();
    }

    @Bean
    public Step studentMigrationChunkStep() {
        return stepBuilderFactory.get("student_migration_step")
                .chunk(100)
                .reader()
                .processor()
                .writer()
                .listener()
                .build();
    }
}
