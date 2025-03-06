package com.example.addressbook.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ContactDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
}
