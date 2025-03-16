package com.spamdetector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/*
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan
 */
@SpringBootApplication
public class SpamDetectorApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpamDetectorApiApplication.class, args);
	}

}
