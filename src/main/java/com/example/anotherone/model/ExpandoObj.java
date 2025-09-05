package com.example.anotherone.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "RegisterUser")
public class ExpandoObj{
    @Id
    public String email;
    public String verificationCode;
    public boolean verified;
    public String f_Firstname;
    public String f_Lastname;
    public String f_password;
//    public UserCRUDGenModal user;

    private String message;


    public ExpandoObj(){

    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getMessage(){
        return message;
    }


}
