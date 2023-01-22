package com.ngyewkong.springbatchdatabasemigration.config;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {
    // setting the DataSourceProperties to prevent error arising from InitializeDataSourceBuilder
    // set your own DataSourceProperties override the default DataSourceBuilder
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSourceProperties batchMetaDataSourceProperties() {
        return new DataSourceProperties();
    }

    // setting the datasource that is used to store spring batch metadata
    // use @Primary annotation
    @Bean("batchMetaDataSource")
    @Primary
    public DataSource batchMetaDataSource() {
        return batchMetaDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.sourcedatasource")
    public DataSourceProperties sourceDataSourceProperties() {
        return new DataSourceProperties();
    }
    // actual table for data
    @Bean("sourceDataSource")
    public DataSource sourceDataSource() {
        return sourceDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.migrationdatasource")
    public DataSourceProperties migrationDataSourceProperties() { return new DataSourceProperties(); }

    @Bean("migrationDataSource")
    public DataSource migrationDataSource() {
        return migrationDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    // setting up the EntityManagerFactory for postgresql & mysql
    // required for JPA
    // used in JPA Item Reader and Writer
    // must be public
    @Bean
    public EntityManagerFactory postgresqlEntityManagerFactory() {
        // using LocalContainerEntityManagerFactoryBean
        LocalContainerEntityManagerFactoryBean lem = new LocalContainerEntityManagerFactoryBean();

        lem.setDataSource(sourceDataSource());
        // scan packages for all the entity classes in postgresql
        lem.setPackagesToScan("com.ngyewkong.springbatchdatabasemigration.entity.postgresql");
        // using Hibernate along with jpa
        lem.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        // set the PersistenceProviderClass (using Hibernate so point to HibernatePersistenceProvider)
        lem.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        // set the properties before returning the object
        lem.afterPropertiesSet();

        // return the object of the LocalContainerEntityManagerFactoryBean which is an EntityManagerFactory
        return lem.getObject();
    }

    // mysql EntityManagerFactory
    @Bean
    public EntityManagerFactory mysqlEntityManagerFactory() {
        // using LocalContainerEntityManagerFactoryBean
        LocalContainerEntityManagerFactoryBean lem = new LocalContainerEntityManagerFactoryBean();

        lem.setDataSource(migrationDataSource());
        // scan packages for all the entity classes in mysql
        lem.setPackagesToScan("com.ngyewkong.springbatchdatabasemigration.entity.mysql");
        // using Hibernate along with jpa
        lem.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        // set the PersistenceProviderClass (using Hibernate so point to HibernatePersistenceProvider)
        lem.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        // set the properties before returning the object
        lem.afterPropertiesSet();

        // return the object of the LocalContainerEntityManagerFactoryBean which is an EntityManagerFactory
        return lem.getObject();
    }

}
