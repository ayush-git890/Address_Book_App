package com.example.addressbook.controller;

import com.example.addressbook.model.Contact;
import com.example.addressbook.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contacts")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    @GetMapping
    public ResponseEntity<List<Contact>> getAllContacts() {
        return ResponseEntity.ok(addressBookService.getAllContacts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContactById(@PathVariable Long id) {
        Contact contact = addressBookService.getContactById(id);
        return contact != null ? ResponseEntity.ok(contact) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Contact> addContact(@RequestBody Contact contact) {
        return ResponseEntity.ok(addressBookService.addContact(contact));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contact> updateContact(@PathVariable Long id, @RequestBody Contact contact) {
        Contact updatedContact = addressBookService.updateContact(id, contact);
        return updatedContact != null ? ResponseEntity.ok(updatedContact) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        addressBookService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }
}
