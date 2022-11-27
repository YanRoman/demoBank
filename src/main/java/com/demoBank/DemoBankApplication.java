package com.demoBank;

import com.demoBank.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoBankApplication.class, args);
	}

}
