/*
package com.example.anotherone.service;

import com.example.anotherone.model.User;
import com.example.anotherone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    public Optional<User> updateUser(int id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            user.setEmail(updatedUser.getEmail());
            user.setCarRegistrationPlate(updatedUser.getCarRegistrationPlate());
            return userRepository.save(user);
        });
    }

    public Optional<User> updateCarRegistrationPlate(int id, String carPlate) {
        return userRepository.findById(id).map(user -> {
            user.setCarRegistrationPlate(carPlate);
            return userRepository.save(user);
        });
    }

    public boolean deleteUser(int id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean carPlateExists(String carPlate) {
        return userRepository.existsByCarRegistrationPlate(carPlate);
    }
}
*/

package com.example.anotherone.service;

import com.example.anotherone.model.User;
import com.example.anotherone.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Create or Update User
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by id
    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    // Update user (full update)
    public Optional<User> updateUser(int id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            user.setEmail(updatedUser.getEmail());
            user.setCarRegistrationPlate(updatedUser.getCarRegistrationPlate());
            return userRepository.save(user);
        });
    }

    // Update only car registration plate
    public Optional<User> updateCarRegistrationPlate(int id, String carRegistrationPlate) {
        return userRepository.findById(id).map(user -> {
            user.setCarRegistrationPlate(carRegistrationPlate);
            return userRepository.save(user);
        });
    }

    // Delete user by id
    public boolean deleteUser(int id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
