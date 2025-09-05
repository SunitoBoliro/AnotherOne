package com.example.anotherone.repository;

import com.example.anotherone.model.ExpandoObj;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepoReg extends MongoRepository<ExpandoObj, String> {
    ExpandoObj findByVerificationCode(String code);  // find user by verification code

    ExpandoObj findByEmail(String email);

//    ExpandoObj findByCarRegistrationNumber(String carRegistrationPlate);

}

