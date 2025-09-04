package com.example.anotherone.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Document(collection = "RegisterUser")
public class ExpandoObj{
    @Id
    public String id;
    public String verificationCode;
    public boolean verified;
    public UserCRUDGenModal user;

}
