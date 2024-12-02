package com.spamdetector.service;

import com.spamdetector.dto.ContactDTO;
import java.util.List;

public interface ContactService {
    ContactDTO addContact(Long userId, ContactDTO contactDTO);
    List<ContactDTO> getUserContacts(Long userId);
    ContactDTO updateContact(Long userId, Long contactId, ContactDTO contactDTO);
    void deleteContact(Long userId, Long contactId);
    void deleteAllContacts();
}