package com.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class AdminProManagerApplication {

	@Autowired
	private BCryptPasswordEncoder passEncoder;

	public static void main(String[] args) {
		SpringApplication.run(AdminProManagerApplication.class, args);
	}

	public void run(String... args) throws Exception {

		String password = "12345";

		for (int i = 0; i < 2; i++) {
			String bcryptPassword = passEncoder.encode(password);
			System.out.println(bcryptPassword);
		}
	}

}
