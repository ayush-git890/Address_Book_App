package com.example.addressbook.service;

import org.springframework.stereotype.Service;
import com.example.addressbook.dto.ContactDTO;
import com.example.addressbook.model.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ContactService {

    private List<Contact> contactList = new ArrayList<>();

    // Add Contact
    public Contact addContact(ContactDTO contactDTO) {
        Contact contact = new Contact(contactList.size() + 1L,
                contactDTO.getName(),
                contactDTO.getEmail());
        contactList.add(contact);
        return contact;
    }

    // Get All Contacts
    public List<Contact> getAllContacts() {
        return contactList;
    }

    // Get Contact By ID
    public Optional<Contact> getContactById(Long id) {
        return contactList.stream()
                .filter(contact -> contact.getId().equals(id))
                .findFirst();
    }

    // Update Contact By ID
    public Contact updateContact(Long id, ContactDTO contactDTO) {
        Optional<Contact> existingContact = getContactById(id);
        if (existingContact.isPresent()) {
            Contact contact = existingContact.get();
            contact.setName(contactDTO.getName());
            contact.setEmail(contactDTO.getEmail());
            return contact;
        }
        return null;
    }

    // Delete Contact By ID
    public void deleteContact(Long id) {
        contactList.removeIf(contact -> contact.getId().equals(id));
    }
}