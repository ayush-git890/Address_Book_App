package com.example.addressbook.repository;

import com.example.addressbook.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressBookRepository extends JpaRepository<Contact, Long> {
}
