package com.example.anotherone.repository;

import com.example.anotherone.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, Integer> {
    // custom query methods if needed
}