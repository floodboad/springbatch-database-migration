package com.ngyewkong.springbatchdatabasemigration.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

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

}
