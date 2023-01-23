package com.ngyewkong.springbatchdatabasemigration.config;

import com.ngyewkong.springbatchdatabasemigration.entity.postgresql.Student;
import com.ngyewkong.springbatchdatabasemigration.listener.StudentStepListener;
import com.ngyewkong.springbatchdatabasemigration.processor.StudentItemProcessor;
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

    // autowired the transaction manager for destination db
    @Autowired
    private JpaTransactionManager jpaTransactionManager;

    @Autowired
    private StudentStepListener studentStepListener;

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
                .<Student, com.ngyewkong.springbatchdatabasemigration.entity.mysql.Student>chunk(100)
                .reader(studentJpaCursorItemReader())
                .processor(studentItemProcessor)
                .writer(studentJpaItemWriter())
                .faultTolerant()
                .skip(Throwable.class)
                .skipLimit(10)
                .retryLimit(2)
                .retry(Throwable.class)
                .listener(studentStepListener)
                // provide the transaction manager in the step
                .transactionManager(jpaTransactionManager)
                .build();
    }

    // Student JPA Item Reader
    // making migration dynamic -> use job Parameters (curr item count and max item count)
    @StepScope
    @Bean
    public JpaCursorItemReader<Student> studentJpaCursorItemReader() {
        JpaCursorItemReader<Student> jpaCursorItemReader = new JpaCursorItemReader<>();

        // set the source emf which has the setup config in DatabaseConfig
        jpaCursorItemReader.setEntityManagerFactory(postgresEMF);

        // provide the jpa query JPQL
        // From the Student Entity class
        // follows the mapping in entity not db
        jpaCursorItemReader.setQueryString("From Student");

        return jpaCursorItemReader;
    }

    // Student JPA Item Writer
    // writer generic is the output entity class
    @Bean
    public JpaItemWriter<com.ngyewkong.springbatchdatabasemigration.entity.mysql.Student> studentJpaItemWriter() {
        JpaItemWriter<com.ngyewkong.springbatchdatabasemigration.entity.mysql.Student> jpaItemWriter = new JpaItemWriter<>();

        // set the migration emf which has the setup config in DatabaseConfig
        jpaItemWriter.setEntityManagerFactory(mysqlEMF);

        // no need for INSERT Query as we are using JPA
        // but we need a transaction as we are persisting data into db
        // use the TransactionManagerBean which is setup in DatabaseConfig

        return jpaItemWriter;
    }

    // autowired the Student Item Processor
    @Autowired
    private StudentItemProcessor studentItemProcessor;

}
