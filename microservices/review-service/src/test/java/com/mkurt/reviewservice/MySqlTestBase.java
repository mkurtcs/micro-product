package com.mkurt.reviewservice;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

public class MySqlTestBase {

    private final static MySQLContainer database = new MySQLContainer("mysql:5.7.32");

    static {
        database.start(); // is used to start the database container before any JUnit code is invoked.
    }

    /**
     * The database container will get some properties defined when started up,
     * such as which port to use. To register these dynamically created properties in the application context,
     * a static method databaseProperties() is defined.
     * The method is annotated with @DynamicPropertySource to override the database configuration
     * in the application context, such as the configuration from an application.yml file.
     */
    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", database::getJdbcUrl);
        registry.add("spring.datasource.username", database::getUsername);
        registry.add("spring.datasource.password", database::getPassword);
    }
}
