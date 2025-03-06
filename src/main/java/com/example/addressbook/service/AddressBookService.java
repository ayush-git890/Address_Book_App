package com.example.addressbook.service;

import com.example.addressbook.dto.ContactDTO;
import com.example.addressbook.model.Contact;
import com.example.addressbook.repository.AddressBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressBookService {

    @Autowired
    private AddressBookRepository repository;

    private ContactDTO convertToDTO(Contact contact) {
        return new ContactDTO(contact.getId(), contact.getName(), contact.getEmail(), contact.getPhone());
    }

    private Contact convertToEntity(ContactDTO contactDTO) {
        Contact contact = new Contact();
        contact.setId(contactDTO.getId()); // Ensure ID is set (in case of update)
        contact.setName(contactDTO.getName());
        contact.setEmail(contactDTO.getEmail());
        contact.setPhone(contactDTO.getPhone());
        return contact;
    }

    public List<ContactDTO> getAllContacts() {
        return repository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ContactDTO getContactById(Long id) {
        return repository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public ContactDTO addContact(ContactDTO contactDTO) {
        Contact contact = convertToEntity(contactDTO);
        Contact savedContact = repository.save(contact);
        return convertToDTO(savedContact);
    }

    public ContactDTO updateContact(Long id, ContactDTO contactDTO) {
        if (repository.existsById(id)) {
            Contact contact = convertToEntity(contactDTO);
            contact.setId(id); // Ensure ID is preserved
            Contact updatedContact = repository.save(contact);
            return convertToDTO(updatedContact);
        }
        return null;
    }

    public void deleteContact(Long id) {
        repository.deleteById(id);
    }
}
