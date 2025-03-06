package com.example.addressbook.service;

import com.example.addressbook.model.Contact;
import com.example.addressbook.repository.AddressBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressBookService {

    @Autowired
    private AddressBookRepository repository;

    public List<Contact> getAllContacts() {
        return repository.findAll();
    }

    public Contact getContactById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Contact addContact(Contact contact) {
        return repository.save(contact);
    }

    public Contact updateContact(Long id, Contact contact) {
        if (repository.existsById(id)) {
            contact.setId(id);
            return repository.save(contact);
        }
        return null;
    }

    public void deleteContact(Long id) {
        repository.deleteById(id);
    }
}
