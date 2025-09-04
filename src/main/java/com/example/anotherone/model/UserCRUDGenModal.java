package com.example.anotherone.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashMap;
import java.util.Map;

public class UserCRUDGenModal {
    public String f_name;
    public String f_email;
    public String f_password;

    // Default constructor required by Spring Data
    public UserCRUDGenModal() {}

    // Parameterized constructor
    public UserCRUDGenModal(String f_name, String f_email, String f_password) {
        this.f_name = f_name;
        this.f_email = f_email;
        this.f_password = f_password;
    }
}
