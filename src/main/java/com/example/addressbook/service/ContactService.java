package com.example.addressbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.example.addressbook.dto.ContactDTO;
import com.example.addressbook.event.EventPublisher;
import com.example.addressbook.model.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class ContactService {

    private List<Contact> contactList = new ArrayList<>();

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private EventPublisher eventPublisher;

    private static final String CACHE_KEY = "ContactList";

    // Add Contact
    public Contact addContact(ContactDTO contactDTO) {
        Contact contact = new Contact(contactList.size() + 1L,
                contactDTO.getName(),
                contactDTO.getEmail());
        contactList.add(contact);

        // Clear Redis Cache
        redisTemplate.delete(CACHE_KEY);

        // 🔥 Publish Event to RabbitMQ
        eventPublisher.publishContactAddedEvent(contactDTO.getName());

        return contact;
    }

    // Get All Contacts with Redis Cache
    public List<Contact> getAllContacts() {
        List<Contact> cachedContacts = (List<Contact>) redisTemplate.opsForValue().get(CACHE_KEY);

        if (cachedContacts != null) {
            System.out.println("Fetching from Redis Cache ");
            return cachedContacts;
        }

        System.out.println("Fetching from Memory ");
        redisTemplate.opsForValue().set(CACHE_KEY, contactList, 5, TimeUnit.MINUTES);
        return contactList;
    }

    // Get Contact By ID
    public Optional<Contact> getContactById(Long id) {
        return getAllContacts().stream()
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
            redisTemplate.delete(CACHE_KEY);
            return contact;
        }
        return null;
    }

    // Delete Contact By ID
    public void deleteContact(Long id) {
        contactList.removeIf(contact -> contact.getId().equals(id));
        redisTemplate.delete(CACHE_KEY);
    }
}