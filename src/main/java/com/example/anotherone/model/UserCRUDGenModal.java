package com.example.anotherone.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashMap;
import java.util.Map;

public class UserCRUDGenModal {

    public String f_email;
    public String f_Firstname;
    public String f_Lastname;
    public String f_password;

    public String carRegistrationPlate;


    // Default constructor required by Spring Data
    public UserCRUDGenModal() {}

    // Parameterized constructor
    public UserCRUDGenModal(String f_name, String f_email, String f_password, String car_registration_plate) {
        this.f_Firstname = f_Firstname;
        this.f_Lastname =  f_Lastname;
        this.f_password = f_password;
        this.carRegistrationPlate = carRegistrationPlate;
    }
}
