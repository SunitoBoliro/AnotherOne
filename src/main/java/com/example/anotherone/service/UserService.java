package com.example.anotherone.service;

import com.example.anotherone.model.ExpandoObj;
import com.example.anotherone.model.User;
import com.example.anotherone.model.UserCRUDGenModal;
import com.example.anotherone.repository.UserRepoReg;
import com.example.anotherone.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final SecureRandom random = new SecureRandom();

    private final UserRepository userRepository;
    private final UserRepoReg userRepoReg;

    public UserService(UserRepository userRepository, UserRepoReg userRepoReg) {
        this.userRepository = userRepository;
        this.userRepoReg = userRepoReg;
    }

    // ----------------- CRUD Operations -----------------

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public Optional<User> updateUser(String id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            user.setEmail(updatedUser.getEmail());
            user.setCarRegistrationPlate(updatedUser.getCarRegistrationPlate());
            return userRepository.save(user);
        });
    }

    public Optional<User> updateCarRegistrationPlate(String id, String carRegistrationPlate) {
        return userRepository.findById(id).map(user -> {
            user.setCarRegistrationPlate(carRegistrationPlate);
            return userRepository.save(user);
        });
    }

    public boolean deleteUser(String id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // ----------------- Registration & Verification -----------------

    public Object registerNewUser(UserCRUDGenModal user) {
        String email = user.f_email;

        // Check if email already exists
        if (userRepoReg.findByEmail(email) != null) {
            ExpandoObj ex = new ExpandoObj();
            ex.setMessage("User Already Exist, Just Need to Verify!");
            return ex.getMessage();
        }

        // Create registration object
        ExpandoObj expandoObj = new ExpandoObj();
        expandoObj.verificationCode = generateVerificationCode();
        expandoObj.verified = false;
        expandoObj.f_Firstname = user.f_Firstname;
        expandoObj.f_Lastname = user.f_Lastname;
        expandoObj.email = user.f_email;
        expandoObj.f_password = user.f_password;


        // Save and return
        return userRepoReg.save(expandoObj);
    }

    public Object verifyUser(String code) {
        ExpandoObj expandoObj = userRepoReg.findByVerificationCode(code);
        System.out.println(expandoObj);

        if (expandoObj == null) {
            ExpandoObj response = new ExpandoObj();
            response.setMessage("No user found with verification code: " + code);
            return response.getMessage();
        }

        if (!expandoObj.verified) {
            expandoObj.verified = true;
            expandoObj.verificationCode = "VERIFIED";
            userRepoReg.save(expandoObj);
            expandoObj.setMessage("User verified successfully: " + expandoObj.email);
        } else {
            expandoObj.setMessage("User already verified: " + expandoObj.email);
        }

        return expandoObj.getMessage();
    }

    // ----------------- Utility Methods -----------------

    private String generateVerificationCode() {
        return String.format("%06d", random.nextInt(1_000_000)); // 6-digit code
    }

//    public boolean carPlateExists(String carPlate) {
//        return userRepository.existsByCarRegistrationPlate(carPlate);
//    }

    // ----------------- Custom Exceptions -----------------

    public static class EmailAlreadyExistsException extends Exception {
        public EmailAlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class UserNotFoundException extends Exception {
        public UserNotFoundException(String message) {
            super(message);
        }
    }
}
