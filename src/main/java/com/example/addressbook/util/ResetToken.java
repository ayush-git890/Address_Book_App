package com.example.addressbook.util;

import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class ResetToken {
    public String generateResetToken() {
        return UUID.randomUUID().toString();
    }
}
