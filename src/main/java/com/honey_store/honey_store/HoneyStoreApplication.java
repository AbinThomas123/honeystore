package com.honey_store.honey_store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
public class HoneyStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(HoneyStoreApplication.class, args);
	}

}
