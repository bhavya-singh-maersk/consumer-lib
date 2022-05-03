package com.maersk.consumerlib;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;



@Slf4j
@EnableAsync
@SpringBootApplication
public class ConsumerLibApplication{

	public static void main(String[] args) {
		SpringApplication.run(ConsumerLibApplication.class, args);
	}
}
