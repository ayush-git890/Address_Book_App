package com.example.addressbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.addressbook.model.Contact;

import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    Optional<Contact> findByEmail(String email);
    Optional<Contact> findByResetToken(String resetToken);
}