package com.mkurt.productservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.mkurt")
public class ProductServiceApplication {

	private static final Logger LOG = LoggerFactory.getLogger(ProductServiceApplication.class);


	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(ProductServiceApplication.class, args);

		String mongoHost = ctx.getEnvironment().getProperty("spring.data.mongodb.host");
		String mongoPort = ctx.getEnvironment().getProperty("spring.data.mongodb.port");
		/**
		 * When scaling up the number of microservices where each microservice connects to its own database,
		 * it can be hard to keep track of what database each microservice actually uses. To avoid this confusion,
		 * a good practice is to add a log statement directly after the startup of a microservice that logs connection
		 * information that is used to connect to the database.
		 */
		LOG.info("Connected to MongoDB: " + mongoHost + ":" + mongoPort);
	}

}
