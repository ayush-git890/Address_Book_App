package com.example.addressbook.event;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.example.addressbook.config.RabbitMQConfig.*;

@Component
public class EventListener {

    // Consume User Registration Event
    @RabbitListener(queues = USER_REGISTRATION_QUEUE)
    public void handleUserRegistrationEvent(String email) {
        System.out.println("ending Registration Email to: " + email);
    }

    // Consume Contact Added Event
    @RabbitListener(queues = CONTACT_ADDED_QUEUE)
    public void handleContactAddedEvent(String contactName) {
        System.out.println("New Contact Added: " + contactName);
    }
}