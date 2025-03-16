package com.example.addressbook.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactDTO{
    private String name;
    private String email;
    private String password;
}