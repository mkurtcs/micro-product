package com.mkurt.recommendationservice;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;

public class MongoDbTestBase {

    private static MongoDBContainer database = new MongoDBContainer("mongo:4.4.2");

    static {
        database.start();
    }

    /**
     * The database container will get some properties defined when started up,
     * such as which port to use. To register these dynamically created properties in the application context,
     * a static method databaseProperties() is defined.
     * The method is annotated with @DynamicPropertySource to override the database configuration
     * in the application context, such as the configuration from an application.yml file.
     */
    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.host", database::getContainerIpAddress);
        registry.add("spring.data.mongodb.port", () -> database.getMappedPort(27017));
        registry.add("spring.data.mongodb.database", () -> "test");
    }
}
