package com.example.addressbook.event;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.addressbook.config.RabbitMQConfig.*;

@Service
public class EventPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // Publish Event for New User Registration
    public void publishUserRegistrationEvent(String email) {
        rabbitTemplate.convertAndSend(USER_REGISTRATION_QUEUE, email);
    }

    // Publish Event for New Contact Added
    public void publishContactAddedEvent(String contactName) {
        rabbitTemplate.convertAndSend(CONTACT_ADDED_QUEUE, contactName);
    }
}