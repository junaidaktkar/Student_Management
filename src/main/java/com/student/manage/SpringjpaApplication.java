package com.student.manage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableCaching
public class SpringjpaApplication {

	public static void main(String[] args) {
		 ApplicationContext context = SpringApplication.run(SpringjpaApplication.class, args);
		 System.out.println("Server is running....");
	}

}
