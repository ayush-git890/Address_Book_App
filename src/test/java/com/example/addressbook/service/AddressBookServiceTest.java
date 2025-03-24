package com.example.addressbook.service;

import com.example.addressbook.dto.AddressBookDTO;
import com.example.addressbook.model.AddressBook;
import com.example.addressbook.repository.AddressBookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressBookServiceTest {

    @Mock
    private AddressBookRepository addressBookRepository;

    @InjectMocks
    private AddressBookService addressBookService;

    private ModelMapper modelMapper = new ModelMapper();

    private AddressBook addressBook;
    private AddressBookDTO addressBookDTO;

    @BeforeEach
    void setUp() {
        addressBook = new AddressBook();
        addressBook.setId(1L);
        addressBook.setFirstName("Ayush");
        addressBook.setLastName("Agarwal");
        addressBook.setEmail("ayush@example.com");
        addressBook.setAddress("Agra, India");
        addressBook.setPhoneNumber(9876543210L);

        addressBookDTO = modelMapper.map(addressBook, AddressBookDTO.class);
    }

    @Test
    void testGetAddressBookData() {
        when(addressBookRepository.findAll()).thenReturn(Arrays.asList(addressBook));

        List<AddressBookDTO> result = addressBookService.getAddressBookData();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Ayush", result.get(0).getFirstName());
        assertEquals("Agarwal", result.get(0).getLastName());
        assertEquals("ayush@example.com", result.get(0).getEmail());
    }

    @Test
    void testGetAddressBookDataById() {
        when(addressBookRepository.findById(1L)).thenReturn(Optional.of(addressBook));

        AddressBookDTO result = addressBookService.getAddressBookDataById(1L);

        assertNotNull(result);
        assertEquals("Ayush", result.getFirstName());
        assertEquals("Agarwal", result.getLastName());
        assertEquals("ayush@example.com", result.getEmail());
    }

    @Test
    void testCreateAddressBookData() {
        when(addressBookRepository.save(any(AddressBook.class))).thenReturn(addressBook);

        AddressBookDTO result = addressBookService.createAddressBookData(addressBookDTO);

        assertNotNull(result);
        assertEquals("Ayush", result.getFirstName());
        assertEquals("Agarwal", result.getLastName());
        assertEquals("ayush@example.com", result.getEmail());
    }

    @Test
    void testUpdateAddressBookData() {
        when(addressBookRepository.findById(1L)).thenReturn(Optional.of(addressBook));
        when(addressBookRepository.save(any(AddressBook.class))).thenReturn(addressBook);

        boolean result = addressBookService.updateAddressBookData(1L, addressBookDTO);

        assertTrue(result);
    }

    @Test
    void testDeleteAddressBookData() {
        doNothing().when(addressBookRepository).deleteById(1L);

        assertDoesNotThrow(() -> addressBookService.deleteAddressBookData(1L));

        verify(addressBookRepository, times(1)).deleteById(1L);
    }
}