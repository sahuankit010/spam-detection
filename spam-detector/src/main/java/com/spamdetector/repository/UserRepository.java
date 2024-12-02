package com.spamdetector.repository;

import com.spamdetector.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User> findByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);

    // New methods for search
    List<User> findByNameStartingWithIgnoreCase(String name);
    List<User> findByNameContainingIgnoreCase(String name);
}
