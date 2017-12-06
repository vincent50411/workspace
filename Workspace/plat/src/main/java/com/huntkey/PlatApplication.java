package com.huntkey;

import com.huntkey.controller.SerializrableClassTest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PlatApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlatApplication.class, args);


		try {
			SerializrableClassTest.mainTest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
