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
public class SubjectMigrationJobConfig {
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
    public Job subjectMigrationJob() {
        return jobBuilderFactory.get("subject_migration_job")
                .incrementer(new RunIdIncrementer())
                .start(subjectMigrationChunkStep())
                .build();
    }

    @Bean
    public Step subjectMigrationChunkStep() {
        return stepBuilderFactory.get("subject_migration_step")
                .chunk(100)
                .reader()
                .processor()
                .writer()
                .listener()
                .build();
    }
}
