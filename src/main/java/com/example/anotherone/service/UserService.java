package com.example.anotherone.service;

import com.example.anotherone.EmailService.EmailService;
import com.example.anotherone.JwtUtil;
import com.example.anotherone.model.ExpandoObj;
import com.example.anotherone.model.UserCRUDGenModal;
import com.example.anotherone.repository.UserRepoReg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Objects;

@Service
public class UserService {

    private static final SecureRandom random = new SecureRandom();
    private final UserRepoReg userRepoReg;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepoReg userRepoReg, EmailService emailService, JwtUtil jwtUtil) {
        this.userRepoReg = userRepoReg;
        this.emailService = emailService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = new BCryptPasswordEncoder(); // default strength
    }
    // ----------------- Registration & Verification -----------------

    // ----------------- Free Wash Code -----------------

    public Object enduseFreeWash(String email, String carRegNum, String code) {

        ExpandoObj user = userRepoReg.findByEmail(email);
        if (user == null) {
            return Map.of("status", "error", "message", "User not found");
        }

        // 2. Check if user has a car registered
        String carObj = user.carRegistrationPlate;
        if (carObj == null) {
            return Map.of("status", "error", "message", "Car registration not found");
        }

        // 3. Check if free wash is available
        if (user.freeWashCount == 1 && Objects.equals(user.freewashCode, code)) {
            // 4. Generate 6-digit free wash code

            // 5. Mark as used
            user.freeWashCount = 0;
            user.freewashCode = "USED";
            userRepoReg.save(user);

            return Map.of(
                    "status", "success",
                    "message", "You have Now Used your Free Wash",
                    "freeWashCode", user.freewashCode
            );
        } else {
            return Map.of("status", "error", "message", "You have already used your free wash");
        }
    }
    public Object useFreeWash(String email, String carRegNumber) {
        // 1. Find user by email
        ExpandoObj user = userRepoReg.findByEmail(email);
        if (user == null) {
            return Map.of("status", "error", "message", "User not found");
        }

        // 2. Check if user has a car registered
        String carObj = user.carRegistrationPlate;
        if (carObj == null) {
            return Map.of("status", "error", "message", "Car registration not found");
        }

        // 3. Check if free wash is available
        if (user.freeWashCount == 2) {
            // 4. Generate 6-digit free wash code
            String freeWashCode = generateVerificationCode();

            // 5. Mark as used
            user.freeWashCount = 1;
            user.freewashCode = freeWashCode;
            userRepoReg.save(user);

            return Map.of(
                    "status", "success",
                    "message", "Enjoy your free wash!",
                    "freeWashCode", freeWashCode
            );
        } else {
            return Map.of("status", "error", "message", "You have already used your free wash");
        }
    }

    public Object login(String email, String password) {
        ExpandoObj user = userRepoReg.findByEmail(email);

        if (user == null) {
            return Map.of("status", "error", "message", "User not found");
        }
        if (!user.verified) {
            return Map.of("status", "error", "message", "User not verified. Check email.");
        }
        if (!passwordEncoder.matches(password, user.f_password)) {
            return Map.of("status", "error", "message", "Invalid credentials");
        }

        String token = jwtUtil.generateToken(email);
        return Map.of("status", "success", "token", token);
    }


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
        expandoObj.f_password = passwordEncoder.encode(user.f_password);
        expandoObj.carRegistrationPlate = user.carRegistrationPlate;

        ExpandoObj saved = userRepoReg.save(expandoObj);
        emailService.sendVerificationEmail(user.f_email, expandoObj.verificationCode);

        saved.setMessage("Verification code sent to: " + user.f_email);
        return saved;
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
