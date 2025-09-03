/*
package com.example.anotherone.controller;

import com.example.anotherone.model.User;
import com.example.anotherone.service.JwtService;
import com.example.anotherone.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User API", description = "CRUD operations with JWT for car plate")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register user with JWT")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (userService.carPlateExists(user.getCarRegistrationPlate())) {
            return ResponseEntity.badRequest().body("Car registration plate already used");
        }
        User savedUser = userService.saveUser(user);
        String token = jwtService.generateToken(savedUser.getCarRegistrationPlate());
        return ResponseEntity.ok("User created. JWT: " + token);
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

    @PutMapping("/{id}/carPlate")
    @Operation(summary = "Update user car plate with JWT validation")
    public ResponseEntity<?> updateCarPlate(@PathVariable int id,
                                            @RequestParam String newCarPlate,
                                            @RequestParam String token) {
        if (!jwtService.validateToken(token)) {
            return ResponseEntity.badRequest().body("Token expired or invalid. Request a new one.");
        }

        if (userService.carPlateExists(newCarPlate)) {
            return ResponseEntity.badRequest().body("Car registration plate already used");
        }

        Optional<User> updated = userService.updateCarRegistrationPlate(id, newCarPlate);
        if (updated.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        String newToken = jwtService.generateToken(newCarPlate);
        return ResponseEntity.ok("Car plate updated. New JWT: " + newToken);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        boolean deleted = userService.deleteUser(id);
        return deleted ? ResponseEntity.ok("User deleted") : ResponseEntity.badRequest().body("User not found");
    }
}
*/

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
    public Optional<User> getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @PostMapping
    @Operation(summary = "Create a new user")
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user); // ✅ now it calls createUser, not saveUser
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a user by ID")
    public Optional<User> updateUser(@PathVariable String id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @PutMapping("/{id}/car-plate")
    @Operation(summary = "Update only the car registration plate of a user")
    public Optional<User> updateCarPlate(@PathVariable String id, @RequestParam String carRegistrationPlate) {
        return userService.updateCarRegistrationPlate(id, carRegistrationPlate);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user by ID")
    public String deleteUser(@PathVariable String id) {
        boolean deleted = userService.deleteUser(id);
        return deleted ? "User deleted successfully" : "User not found";
    }
}