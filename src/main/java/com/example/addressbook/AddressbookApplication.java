package com.example.addressbook;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableCaching
public class AddressbookApplication {

	public static void main(String[] args) {
		SpringApplication.run(AddressbookApplication.class, args);
	}

}
