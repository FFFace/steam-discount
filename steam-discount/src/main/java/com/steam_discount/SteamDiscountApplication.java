package com.steam_discount;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class SteamDiscountApplication {

	public static void main(String[] args) {
		SpringApplication.run(SteamDiscountApplication.class, args);
	}

}
