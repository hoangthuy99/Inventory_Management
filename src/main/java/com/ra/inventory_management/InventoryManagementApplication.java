package com.ra.inventory_management;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.converter.JsonMessageConverter;

@SpringBootApplication

public class InventoryManagementApplication {
	public static void main(String[] args) {
		SpringApplication.run(InventoryManagementApplication.class, args);
	}
	@Bean
	JsonMessageConverter messageConverter(){
		return new JsonMessageConverter();
	}
}
