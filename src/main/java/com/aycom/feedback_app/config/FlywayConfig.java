package com.aycom.feedback_app.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.jpa.autoconfigure.EntityManagerFactoryDependsOnPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlywayConfig {

    @Bean(initMethod = "migrate")
    Flyway flyway(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .load();
    }

    // Tells Spring Boot's JPA to wait for the "flyway" bean before creating
    // the EntityManagerFactory, so Hibernate validates AFTER migrations run.
    @Bean
    static EntityManagerFactoryDependsOnPostProcessor flywayJpaDependency() {
        return new EntityManagerFactoryDependsOnPostProcessor("flyway");
    }
}
