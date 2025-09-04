package com.example.anotherone.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ExistingEmails")
public class ExistingEmails {

    public String email;

    public ExistingEmails(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
