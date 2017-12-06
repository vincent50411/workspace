package com.huntkey;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class CloudConfigServerDemoApplication
{

	public static void main(String[] args) {
		SpringApplication.run(CloudConfigServerDemoApplication.class, args);
	}
}
