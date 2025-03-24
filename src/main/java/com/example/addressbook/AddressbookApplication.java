package com.example.addressbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@EnableCaching
public class AddressBookApplication {

    public static void main(String[] args) {

        Dotenv.configure()
                .systemProperties()
                .load();
        SpringApplication.run(AddressBookApplication.class, args);
    }
}