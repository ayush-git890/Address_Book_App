package com.example.addressbook.service;

import com.example.addressbook.dto.AddressBookDTO;
import com.example.addressbook.interfaces.IAddressBookService;
import com.example.addressbook.model.AddressBook;
import com.example.addressbook.repository.AddressBookRepository;
import org.hibernate.annotations.Cache;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressBookService implements IAddressBookService {

    @Autowired
    AddressBookRepository addressBookRepository;

    ModelMapper modelMapper = new ModelMapper();
    @Override
    @Cacheable(value = "addressBookCache")
    public List<AddressBookDTO> getAddressBookData() {
        List<AddressBook> addressBooksLists = addressBookRepository.findAll();
        return addressBooksLists.stream()
                .map(addressBook -> modelMapper.map(addressBook, AddressBookDTO.class))
                .toList();
    }

    @Override
    @Cacheable(value = "addressBookCache", key = "#id")
    public AddressBookDTO getAddressBookDataById(long id) {
        AddressBook addressBook = addressBookRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee Payroll not found with id: " + id));
        return modelMapper.map(addressBook, AddressBookDTO.class);
    }

    @Override
    public AddressBookDTO createAddressBookData(AddressBookDTO addressBookDTO) {
        AddressBook addressBook = addressBookRepository.save(modelMapper.map(addressBookDTO, AddressBook.class));
        return modelMapper.map(addressBook, AddressBookDTO.class);
    }

    @Override
    public boolean updateAddressBookData(long id, AddressBookDTO updatedAddressBookDTO) {
        try {
            AddressBook addressBook = addressBookRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee Payroll not found with id: " + id));
            addressBook.setFirstName(updatedAddressBookDTO.getFirstName());
            addressBook.setLastName(updatedAddressBookDTO.getLastName());
            addressBook.setAddress(updatedAddressBookDTO.getAddress());
            addressBook.setPhoneNumber(updatedAddressBookDTO.getPhoneNumber());
            addressBookRepository.save(addressBook);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void deleteAddressBookData(long id) {
        try {
            addressBookRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Employee Payroll not found with id: " + id);
        }
    }
}