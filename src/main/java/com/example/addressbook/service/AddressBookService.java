package com.example.addressbook.service;

import com.example.addressbook.dto.ContactDTO;
import com.example.addressbook.model.Contact;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AddressBookService {

    private final List<Contact> contactList = new ArrayList<>();
    private Long contactIdCounter = 1L;

    private ContactDTO convertToDTO(Contact contact) {
        return new ContactDTO(contact.getId(), contact.getName(), contact.getEmail(), contact.getPhone());
    }

    private Contact convertToEntity(ContactDTO contactDTO) {
        return new Contact(contactDTO.getId(), contactDTO.getName(), contactDTO.getEmail(), contactDTO.getPhone());
    }

    public List<ContactDTO> getAllContacts() {
        return contactList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public ContactDTO getContactById(Long id) {
        Optional<Contact> contact = contactList.stream().filter(c -> c.getId().equals(id)).findFirst();
        return contact.map(this::convertToDTO).orElse(null);
    }

    public ContactDTO addContact(ContactDTO contactDTO) {
        Contact newContact = convertToEntity(contactDTO);
        newContact.setId(contactIdCounter++); // Auto-increment ID
        contactList.add(newContact);
        return convertToDTO(newContact);
    }

    public ContactDTO updateContact(Long id, ContactDTO contactDTO) {
        for (int i = 0; i < contactList.size(); i++) {
            if (contactList.get(i).getId().equals(id)) {
                Contact updatedContact = convertToEntity(contactDTO);
                updatedContact.setId(id); // Preserve ID
                contactList.set(i, updatedContact);
                return convertToDTO(updatedContact);
            }
        }
        return null;
    }

    public void deleteContact(Long id) {
        contactList.removeIf(contact -> contact.getId().equals(id));
    }
}