package com.example.anotherone.service;

import com.example.anotherone.model.ExistingEmails;
import com.example.anotherone.model.ExpandoObj;
import com.example.anotherone.model.User;
import com.example.anotherone.model.UserCRUDGenModal;
import com.example.anotherone.repository.UserEmailExist;
import com.example.anotherone.repository.UserRepoReg;
import com.example.anotherone.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private static final SecureRandom random = new SecureRandom();

    private final UserRepository userRepository;
    private final UserRepoReg userRepoReg;
    private final UserEmailExist userEmailExist;

    public UserService(UserRepository userRepository, UserRepoReg userRepoReg, UserEmailExist userEmailExist) {
        this.userRepository = userRepository;
        this.userRepoReg = userRepoReg;
        this.userEmailExist = userEmailExist;
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

    public ExpandoObj registerNewUser(UserCRUDGenModal user) throws EmailAlreadyExistsException {
        String email = user.f_email;

        // Check if email already exists
        if (userEmailExist.findByEmail(email).isPresent()) {
            logger.warn("User already exists with email: {}", email);
            throw new EmailAlreadyExistsException("User already exists with email: " + email);
        }

        // Create registration object
        ExpandoObj expandoObj = new ExpandoObj();
        expandoObj.verificationCode = generateVerificationCode();
        expandoObj.verified = false;
        expandoObj.user = user;

        // Save and return
        return userRepoReg.save(expandoObj);
    }

    public ExpandoObj verifyUser(String code) throws UserNotFoundException {
        ExpandoObj expandoObj = userRepoReg.findByVerificationCode(code);
        if (expandoObj == null) {
            logger.warn("No user found with verification code: {}", code);
            throw new UserNotFoundException("No user found with verification code: " + code);
        }

        if (!expandoObj.verified) {
            expandoObj.verified = true;
            expandoObj.verificationCode = "VERIFIED";
            userRepoReg.save(expandoObj);
            userEmailExist.save(new ExistingEmails(expandoObj.user.f_email));
            logger.info("User verified successfully: {}", expandoObj.user.f_email);
        } else {
            logger.info("User already verified: {}", expandoObj.user.f_email);
        }

        return expandoObj;
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
