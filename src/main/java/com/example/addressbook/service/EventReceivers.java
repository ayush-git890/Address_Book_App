package com.example.addressbook.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import static com.example.addressbook.config.RabbitMQConf.QUEUE_NAME;

@Component
public class EventReceivers {

    private final EmailSenderService emailService;
    public EventReceivers(EmailSenderService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = QUEUE_NAME)
    public void receiveMessage(String message) {
        System.out.println("Received message from RabbitMQ: " + message);

        String[] data = message.split("\\|");
        if (data.length < 3) {
            System.err.println("Invalid message format: " + message);
            return;
        }

        String messageType = data[0];
        String email = data[1];
        String firstName = data[2];

        String subject;
        String body = "Hi " + firstName + ",\n";

        switch (messageType) {
            case "REGISTER":
                subject = "Registration Successful";
                body += "You have been successfully registered.";
                sendEmailAndLog(email, subject, body);
                break;

            case "FORGOT":
                subject = "Forgot Password";
                body += "Your password has been changed.";
                sendEmailAndLog(email, subject, body);
                break;

            case "RESET":
                subject = "Reset Password";
                body += "Your password has been reset.";
                sendEmailAndLog(email, subject, body);
                break;

            case "LOGIN":
                subject = "Login Alert";
                body += "You have successfully logged into your account.";
                sendEmailAndLog(email, subject, body);
                System.out.println("User Logged In: " + firstName + " (" + email + ")");
                break;

            default:
                System.err.println("⚠ Unknown message type: " + messageType);
        }
    }

    private void sendEmailAndLog(String email, String subject, String body) {
        emailService.sendEmail(email, subject, body);
        System.out.println("Email sent to: " + email + " | Subject: " + subject);
    }
}