package com.example.anotherone.repository;

import com.example.anotherone.model.ExistingEmails;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserEmailExist extends MongoRepository<ExistingEmails, String> {
    Optional<ExistingEmails> findByEmail(String email);
}
