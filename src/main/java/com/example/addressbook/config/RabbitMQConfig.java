package com.example.addressbook.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String USER_REGISTRATION_QUEUE = "user.registration.queue";
    public static final String CONTACT_ADDED_QUEUE = "contact.added.queue";

    @Bean
    public Queue userRegistrationQueue() {
        return new Queue(USER_REGISTRATION_QUEUE, false);
    }

    @Bean
    public Queue contactAddedQueue() {
        return new Queue(CONTACT_ADDED_QUEUE, false);
    }
}