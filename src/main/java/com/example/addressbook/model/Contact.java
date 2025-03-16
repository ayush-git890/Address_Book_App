package com.example.addressbook.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class Contact{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    private String resetToken; // For Reset Password Token

    public Contact(long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}