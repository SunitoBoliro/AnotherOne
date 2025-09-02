package com.example.anotherone.controller;

import com.example.anotherone.model.User;
import com.example.anotherone.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User API", description = "CRUD operations for User")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Get all users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    public Optional<User> getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @PostMapping
    @Operation(summary = "Create a new user")
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user); // ✅ now it calls createUser, not saveUser
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a user by ID")
    public Optional<User> updateUser(@PathVariable int id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @PutMapping("/{id}/car-plate")
    @Operation(summary = "Update only the car registration plate of a user")
    public Optional<User> updateCarPlate(@PathVariable int id, @RequestParam String carRegistrationPlate) {
        return userService.updateCarRegistrationPlate(id, carRegistrationPlate);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user by ID")
    public String deleteUser(@PathVariable int id) {
        boolean deleted = userService.deleteUser(id);
        return deleted ? "User deleted successfully" : "User not found";
    }
}
