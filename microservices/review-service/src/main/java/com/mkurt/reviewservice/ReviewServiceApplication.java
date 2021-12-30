package com.mkurt.reviewservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@SpringBootApplication
@ComponentScan("com.mkurt")
public class ReviewServiceApplication {

	private static final Logger LOG = LoggerFactory.getLogger(ReviewServiceApplication.class);

	private final Integer threadPoolSize; // specifying the max number of threads in the pool
	private final Integer taskQueueSize; // specifying the max number of tasks that are allowed to be placed in a queue waiting for available threads

	@Autowired
	public ReviewServiceApplication(@Value("${app.threadPoolSize:10}") Integer threadPoolSize,
									@Value("${app.taskQueueSize:100}") Integer taskQueueSize) {
		this.threadPoolSize = threadPoolSize;
		this.taskQueueSize = taskQueueSize;
	}

	public static void main(String[] args) {
		SpringApplication.run(ReviewServiceApplication.class, args);
	}

	/**
	 * First, we configure a scheduler bean and its thread pool in the main class ReviewServiceApplication, as follows:
	 */
	@Bean
	public Scheduler jdbcScheduler() {
		return Schedulers.newBoundedElastic(threadPoolSize, taskQueueSize, "jdbc-pool");
	}

}
