package com.huntkey;

import com.huntkey.com.huntkey.controller.RedisClientController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RedisDemoApplication   implements CommandLineRunner
{

	@Autowired
	private RedisClientController redisClientController;

	public static void main(String[] args) {
		SpringApplication.run(RedisDemoApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception
	{
		redisClientController.test();
	}
}
