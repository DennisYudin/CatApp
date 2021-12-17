package com.yourcodereview.dev.yudin.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = "com.yourcodereview.dev.yudin")
@PropertySource("classpath:test-postgresql.properties")
public class AppConfigTest {

    @Autowired
    private Environment propertyDataHolder;

    @Bean
    public DataSource dataSource() {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        String jdbcDriver = propertyDataHolder.getProperty("jdbc.driver.test");
        String jdbcUrl = propertyDataHolder.getProperty("jdbc.url.test");
        String jdbcUser = propertyDataHolder.getProperty("jdbc.user.test");
        String jdbcPassword = propertyDataHolder.getProperty("jdbc.password.test");

        dataSource.setDriverClassName(jdbcDriver);
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(jdbcUser);
        dataSource.setPassword(jdbcPassword);

        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource());

        return jdbcTemplate;
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {

        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource());

        return transactionManager;
    }
}

