package com.mkurt.springcloud.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class ConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigServerApplication.class, args);
	}

}

/**
 *  By default, a client connects first to the config server to retrieve its configuration. Based on the configuration,
 *  it connects to the discovery server, Netflix Eureka in our case, to register itself. It is also possible to do this
 *  the other way around, that is, the client first connecting to the discovery server to find a config server instance
 *  and then connecting to the config server to get its configuration. There are pros and cons to both approaches.
 *  In this project, the clients will first connect to the config server. With this approach,
 *  it will be possible to store the configuration of the discovery server in the config server.
 */

/**
 * One concern with connecting to the config server first is that the config server can become a single point of failure.
 * If the clients connect first to a discovery server, there can be multiple config server instances registered so that
 * a single point of failure can be avoided.
 */