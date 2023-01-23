package com.ngyewkong.springbatchdatabasemigration.config;

import com.ngyewkong.springbatchdatabasemigration.entity.postgresql.Subject;
import com.ngyewkong.springbatchdatabasemigration.listener.SubjectStepListener;
import com.ngyewkong.springbatchdatabasemigration.processor.SubjectItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;

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

    // autowired the transaction manager for destination db
    @Autowired
    private JpaTransactionManager jpaTransactionManager;

    // autowired subject skip step listener
    @Autowired
    private SubjectStepListener subjectStepListener;

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
                .<Subject, com.ngyewkong.springbatchdatabasemigration.entity.mysql.Subject>chunk(100)
                .reader(subjectJpaCursorItemReader())
                .processor(subjectItemProcessor)
                .writer(subjectJpaItemWriter())
                .faultTolerant()
                .skip(Throwable.class)
                .skipLimit(10)
                .retryLimit(2)
                .retry(Throwable.class)
                .listener(subjectStepListener)
                // provide the transaction manager in the step
                .transactionManager(jpaTransactionManager)
                .build();
    }


    // Subject Item Reader
    @Bean
    @StepScope
    public JpaCursorItemReader<Subject> subjectJpaCursorItemReader() {
        JpaCursorItemReader<Subject> jpaCursorItemReader = new JpaCursorItemReader<>();

        // set the source emf which has the setup config in DatabaseConfig
        jpaCursorItemReader.setEntityManagerFactory(postgresEMF);

        // provide the jpa query JPQL
        // From the Subject Entity class
        // follows the mapping in entity not db
        jpaCursorItemReader.setQueryString("From Subject");

        return jpaCursorItemReader;
    }

    // Subject Item Writer
    @Bean
    @StepScope
    public JpaItemWriter<com.ngyewkong.springbatchdatabasemigration.entity.mysql.Subject> subjectJpaItemWriter() {
        JpaItemWriter<com.ngyewkong.springbatchdatabasemigration.entity.mysql.Subject> jpaItemWriter = new JpaItemWriter<>();

        // set the migration emf which has the setup config in DatabaseConfig
        jpaItemWriter.setEntityManagerFactory(mysqlEMF);

        // no need for INSERT Query as we are using JPA
        // but we need a transaction as we are persisting data into db
        // use the TransactionManagerBean which is setup in DatabaseConfig

        return jpaItemWriter;
    }


    // Subject Item Processor
    @Autowired
    private SubjectItemProcessor subjectItemProcessor;
}
