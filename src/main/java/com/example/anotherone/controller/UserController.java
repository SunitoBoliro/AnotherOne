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

import com.example.anotherone.model.UserCRUDGenModal;
import com.example.anotherone.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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

    @PostMapping("/{reg-user}")
    @Operation(summary = "Create new User with email, password and verification Code")
    public List<Object> registerUser(@RequestBody UserCRUDGenModal userCRUDGenModal) {
        return Collections.singletonList(userService.registerNewUser(userCRUDGenModal));
    }

    @GetMapping("login/{email}/{password}")
    @Operation(summary = "Login with the Same Login Credentials")
    public Object loginUser(@PathVariable String email, @PathVariable String password) {
        return userService.login(email, password);
    }

    @GetMapping("free-wash/{email}/{carRegNum}")
    @Operation(summary = "Login with the Same Login Credentials")
    public Object usefreeWash(@PathVariable String email, @PathVariable String carRegNum) {
        return userService.useFreeWash(email, carRegNum);
    }

    @GetMapping("use-free-wash/{email}/{carRegNum}/{code}")
    @Operation(summary = "Login with the Same Login Credentials")
    public Object usefreeWashend(@PathVariable String email, @PathVariable String carRegNum,  @PathVariable String code) {
        return userService.enduseFreeWash(email, carRegNum, code);
    }

    @GetMapping("/verify/{code}")
    @Operation(summary = "Verify user with code")
    public ResponseEntity<?> verifyUser(@PathVariable String code){
        Object verifiedUser = userService.verifyUser(code);

        if (verifiedUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Invalid verification code");
        }

        return ResponseEntity.ok(verifiedUser);
    }


}