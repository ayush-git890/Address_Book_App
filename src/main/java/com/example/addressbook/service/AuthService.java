package com.example.addressbook.service;

import com.example.addressbook.dto.ContactDTO;
import com.example.addressbook.model.Contact;
import com.example.addressbook.repository.ContactRepository;
import com.example.addressbook.security.JWTUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private ContactRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private EmailService emailService;

    public Contact registerUser(ContactDTO userDTO) {
        Contact user = new Contact();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return userRepository.save(user);
    }

    public String loginUser(String email, String password) {
        Optional<Contact> user = userRepository.findByEmail(email);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return jwtUtil.generateToken(user.get().getEmail());
        }
        return "Invalid Credentials";
    }

    public String forgotPassword(String email) {
        Optional<Contact> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            String token = UUID.randomUUID().toString();
            user.get().setResetToken(token);
            userRepository.save(user.get());

            String resetLink = "http://localhost:8080/api/auth/reset-password?token=" + token;

            // Send Reset Link via Email
            emailService.sendEmail(email, "Reset Your Password", "Click here to reset your password: " + resetLink);

            return "Reset Password Email Sent!";
        }
        return "User Not Found!";
    }

    public String resetPassword(String token, String newPassword) {
        Optional<Contact> user = userRepository.findByResetToken(token);
        if (user.isPresent()) {
            user.get().setPassword(passwordEncoder.encode(newPassword));
            user.get().setResetToken(null); // Remove Token after Reset
            userRepository.save(user.get());
            return "Password Reset Successfully!";
        }
        return "Invalid or Expired Token!";
    }
}