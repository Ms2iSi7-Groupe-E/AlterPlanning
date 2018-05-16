package fr.nantes.eni.alterplanning.config;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "sqlserverEntityManagerFactory",
        transactionManagerRef = "sqlserverTransactionManager",
        basePackages = { "fr.nantes.eni.alterplanning.dao.sqlserver.repository" }
)
public class DBSqlServerConfig {

    @Value("${spring.sqlserver.datasource.url}")
    private String DATASOURCE_URL;

    @Value("${spring.sqlserver.datasource.username}")
    private String DATASOURCE_USERNAME;

    @Value("${spring.sqlserver.datasource.password}")
    private String DATASOURCE_PASSWORD;

    @Value("${hibernate.show_sql}")
    private String HIBERNATE_SHOW_SQL;

    @Value("${hibernate.format_sql}")
    private String HIBERNATE_FORMAT_SQL;

    @Bean
    public DataSource sqlserverDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(SQLServerDriver.class.getName());
        dataSource.setUrl(DATASOURCE_URL);
        dataSource.setUsername(DATASOURCE_USERNAME);
        dataSource.setPassword(DATASOURCE_PASSWORD);

        return dataSource;
    }

    @Bean(name = "sqlserverEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean sqlserverEntityManagerFactory() {
        Properties properties = new Properties();
        properties.put("hibernate.show_sql", HIBERNATE_SHOW_SQL);
        properties.put("hibernate.format_sql", HIBERNATE_FORMAT_SQL);

        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(sqlserverDataSource());
        entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        entityManagerFactoryBean.setPackagesToScan("fr.nantes.eni.alterplanning.dao.sqlserver.entity");

        entityManagerFactoryBean.setJpaProperties(properties);

        return entityManagerFactoryBean;
    }

    @Bean(name = "sqlserverTransactionManager")
    public PlatformTransactionManager sqlserverTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(sqlserverEntityManagerFactory().getObject());
        return transactionManager;
    }
}
