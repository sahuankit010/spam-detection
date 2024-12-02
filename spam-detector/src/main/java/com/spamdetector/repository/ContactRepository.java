package com.spamdetector.repository;

import com.spamdetector.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByUser_Id(Long userId);
    List<Contact> findByPhoneNumber(String phoneNumber);

    // New methods for search
    List<Contact> findByNameStartingWithIgnoreCase(String name);
    List<Contact> findByNameContainingIgnoreCase(String name);

    // Check if a contact exists
    boolean existsByUser_IdAndPhoneNumber(Long userId, String phoneNumber);
}
