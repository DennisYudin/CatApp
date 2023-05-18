package dev.yudin.configs;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import dev.yudin.exceptions.AppConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.beans.PropertyVetoException;
import java.util.Objects;
import java.util.Properties;
import javax.sql.DataSource;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan(basePackages = "dev.yudin")
@PropertySource("classpath:postgresql-connection-test.properties")
public class AppConfigTest {
    public static final String JDBC_DRIVER = "jdbc.driver.test";
    public static final String JDBC_URL = "jdbc.url.test";
    public static final String JDBC_USER = "jdbc.user.test";
    public static final String JDBC_PASSWORD = "jdbc.password.test";
    public static final String PACKAGE_NAME = "dev.yudin";
    public static final String HIBERNATE_DIALECT = "hibernate.dialect";
    public static final String DATA_SOURCE_MIN_POOL_SIZE = "dataSource.minPoolSize";
    public static final String DATA_SOURCE_MAX_POOL_SIZE = "dataSource.maxPoolSize";
    public static final String DATA_SOURCE_MAX_IDLE_TIME = "dataSource.maxIdleTime";
    public static final String HIBERNATE_SHOW_SQL = "hibernate.show_sql";
    @Autowired
    private Environment propHolder;

    @Bean
    public DataSource dataSource() {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();

        String jdbcDriver = propHolder.getProperty(JDBC_DRIVER);
        String jdbcUrl = propHolder.getProperty(JDBC_URL);
        String jdbcUser = propHolder.getProperty(JDBC_USER);
        String jdbcPassword = propHolder.getProperty(JDBC_PASSWORD);

        try {
            dataSource.setDriverClass(jdbcDriver);
        } catch (PropertyVetoException e) {
            throw new AppConfigurationException("Error happened during set Driver class", e);

        }
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUser(jdbcUser);
        dataSource.setPassword(jdbcPassword);
        // connection pool properties for C3PO
        dataSource.setMinPoolSize(mapToInt(propHolder.getProperty(DATA_SOURCE_MIN_POOL_SIZE)));
        dataSource.setMaxPoolSize(mapToInt(propHolder.getProperty(DATA_SOURCE_MAX_POOL_SIZE)));
        dataSource.setMaxIdleTime(mapToInt(propHolder.getProperty(DATA_SOURCE_MAX_IDLE_TIME)));
        return dataSource;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(PACKAGE_NAME);
        sessionFactory.setHibernateProperties(hibernateProperties());

        return sessionFactory;
    }

    private Properties hibernateProperties() {
        Properties prop = new Properties();
        prop.setProperty("hibernate.dialect", propHolder.getProperty(HIBERNATE_DIALECT));
        prop.setProperty("hibernate.show_sql", propHolder.getProperty(HIBERNATE_SHOW_SQL));
        return prop;
    }

    private int mapToInt(String input) {
        return Integer.parseInt(input);
    }

    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager() {
        return new HibernateTransactionManager(Objects.requireNonNull(sessionFactory().getObject()));
    }
}
