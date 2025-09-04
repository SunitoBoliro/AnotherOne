package com.example.anotherone.repository;

import com.example.anotherone.model.User;
import com.example.anotherone.model.UserCRUDGenModal;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, Object> {
}
