package com.example.anotherone.repository;

import com.example.anotherone.model.ExpandoObj;
import com.example.anotherone.model.UserCRUDGenModal;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.HashMap;
import java.util.Map;

public interface UserRepoReg extends MongoRepository<ExpandoObj, String> {
}
