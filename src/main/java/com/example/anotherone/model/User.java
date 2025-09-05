package com.example.anotherone.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String carRegistrationPlate;  // ✅ new field

    // No-args constructor

    // All-args constructor
    public User(String id, String firstName, String lastName, String email, String carRegistrationPlate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.carRegistrationPlate = carRegistrationPlate;
    }

    // Getters & Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCarRegistrationPlate() {
        return carRegistrationPlate;
    }

    public void setCarRegistrationPlate(String carRegistrationPlate) {
        this.carRegistrationPlate = carRegistrationPlate;
    }
}