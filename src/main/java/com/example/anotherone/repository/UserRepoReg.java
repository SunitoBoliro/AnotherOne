package com.example.anotherone.repository;

import com.example.anotherone.model.ExistingEmails;
import com.example.anotherone.model.ExpandoObj;
import com.example.anotherone.model.UserCRUDGenModal;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public interface UserRepoReg extends MongoRepository<ExpandoObj, String> {
    ExpandoObj findByVerificationCode(String code);  // find user by verification code
 }

