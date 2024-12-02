package com.spamdetector.service.impl;

import com.spamdetector.dto.ContactDTO;
import com.spamdetector.entity.Contact;
import com.spamdetector.entity.User;
import com.spamdetector.repository.ContactRepository;
import com.spamdetector.repository.UserRepository;
import com.spamdetector.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactServiceImpl implements ContactService {
    private final ContactRepository contactRepository;
    private final UserRepository userRepository;

    @Autowired
    public ContactServiceImpl(ContactRepository contactRepository, UserRepository userRepository) {
        this.contactRepository = contactRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public ContactDTO addContact(Long userId, ContactDTO contactDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Contact contact = new Contact();
        contact.setName(contactDTO.getName());
        contact.setPhoneNumber(contactDTO.getPhoneNumber());
        contact.setUser(user);

        Contact savedContact = contactRepository.save(contact);
        return convertToDTO(savedContact);
    }

    @Override
    public List<ContactDTO> getUserContacts(Long userId) {
        return contactRepository.findByUser_Id(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ContactDTO updateContact(Long userId, Long contactId, ContactDTO contactDTO) {
        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new RuntimeException("Contact not found"));

        // Ensure the contact belongs to the user
        if (!contact.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized contact modification");
        }

        contact.setName(contactDTO.getName());
        contact.setPhoneNumber(contactDTO.getPhoneNumber());

        Contact updatedContact = contactRepository.save(contact);
        return convertToDTO(updatedContact);
    }

    @Override
    @Transactional
    public void deleteContact(Long userId, Long contactId) {
        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new RuntimeException("Contact not found"));

        // Ensure the contact belongs to the user
        if (!contact.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized contact deletion");
        }

        contactRepository.delete(contact);
    }

    // Helper method to convert Contact to ContactDTO
    private ContactDTO convertToDTO(Contact contact) {
        ContactDTO dto = new ContactDTO();
        dto.setId(contact.getId());
        dto.setName(contact.getName());
        dto.setPhoneNumber(contact.getPhoneNumber());
        return dto;
    }

    public void deleteAllContacts() {
        contactRepository.deleteAll();
    }

}