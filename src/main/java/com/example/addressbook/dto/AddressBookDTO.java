package com.example.addressbook.dto;

import com.example.addressbook.model.AddressBook;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AddressBookDTO class for address book data transfer object.
 * This class is used to validate user input during address book operations.
 * It contains fields for first name, last name, email, address, and phone number.
 * It also includes validation annotations to ensure the data meets certain criteria.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressBookDTO {

    @NotNull(message = "please enter first name")
    @Size(min = 3, max = 30, message = "first name must be between 3 and 30 characters")
    @Pattern(regexp = "^[A-Z][a-zA-Z]*$", message = "first Name should always start with a capital letter and contain only letters")
    private String firstName;

    @NotNull(message = "please enter last name")
    @Pattern(regexp = "^[A-Z][a-zA-Z]*$", message = "last Name should always start with a capital letter and contain only letters")
    @Size(min = 3, max = 30, message = "last name must be between 3 and 30 characters")
    private String lastName;

    @NotNull(message = "please enter address")
    @Pattern(regexp = "^[0-9A-Z][0-9a-zA-Z\\s-/]*$", message = "address should start with a number or capital letter")
    @Size(min = 3, max = 50, message = "address must be between 3 and 30 characters")
    private String address;

    @NotNull(message = "please enter email")
    @Email(message = "email must be valid")
    @Column(unique = true)
    private String email;

    @NotNull(message = "please enter phone")
    @Pattern(regexp = "^[6-9][0-9]{9}$", message = "ph. num. must be 10 digits long")
    private long phoneNumber;
}