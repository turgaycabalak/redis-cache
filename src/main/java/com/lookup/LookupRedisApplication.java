package com.lookup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class LookupRedisApplication {

	public static void main(String[] args) {
		SpringApplication.run(LookupRedisApplication.class, args);
	}

}
