package com.example.anotherone.repository;

import com.example.anotherone.model.UserCRUDGenModal;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepoReg extends MongoRepository<UserCRUDGenModal, String> {
}
