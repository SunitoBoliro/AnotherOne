package com.example.anotherone.service;

import com.example.anotherone.EmailService.EmailService;
import com.example.anotherone.JwtUtil;
import com.example.anotherone.model.ExpandoObj;
import com.example.anotherone.model.UserCRUDGenModal;
import com.example.anotherone.repository.UserRepoReg;
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

    // ----------------- Free Wash Code (Overloaded Methods) -----------------

    public Object useFreeWashValidation(String email, String carRegNumber){
        ExpandoObj user = userRepoReg.findByEmail(email);
        if (user == null) {
            return Map.of("status", "error", "message", "User not found");
        }

        if (user.carRegistrationPlate == null || !user.carRegistrationPlate.equals(carRegNumber)) {
            return Map.of("status", "error", "message", "Car registration not found or mismatched");
        }

        return user;
    }

    // First use: generate a free wash code
    public Object useFreeWash(String email, String carRegNumber) {

        ExpandoObj user = (ExpandoObj) useFreeWashValidation(email, carRegNumber);

        if (user.freeWashCount == 2) {
            String freeWashCode = generateVerificationCode();
            user.freeWashCount = 1; // mark one usage left
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

    // Overloaded: confirm free wash usage with code
    public Object useFreeWash(String email, String carRegNumber, String code) {

        ExpandoObj user = (ExpandoObj) useFreeWashValidation(email, carRegNumber);

        if (user.freeWashCount == 1 && Objects.equals(user.freewashCode, code)) {
            user.freeWashCount = 0; // fully used
            user.freewashCode = "USED";
            userRepoReg.save(user);

            return Map.of(
                    "status", "success",
                    "message", "You have now used your free wash",
                    "freeWashCode", user.freewashCode
            );
        } else {
            return Map.of("status", "error", "message", "Invalid or already used free wash code");
        }
    }

    // ----------------- Login -----------------
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

    // ----------------- Register -----------------
    public Object registerNewUser(UserCRUDGenModal user) {
        String email = user.f_email;

        if (userRepoReg.findByEmail(email) != null) {
            return Map.of("status", "error", "message", "User already exists, please verify");
        }

        ExpandoObj expandoObj = new ExpandoObj();
        expandoObj.verificationCode = generateVerificationCode();
        expandoObj.verified = false;
        expandoObj.f_Firstname = user.f_Firstname;
        expandoObj.f_Lastname = user.f_Lastname;
        expandoObj.email = user.f_email;
        expandoObj.f_password = passwordEncoder.encode(user.f_password);
        expandoObj.carRegistrationPlate = user.carRegistrationPlate;
        expandoObj.freeWashCount = 2; // initialize free washes

        ExpandoObj saved = userRepoReg.save(expandoObj);
        emailService.sendVerificationEmail(user.f_email, expandoObj.verificationCode);

        return Map.of("status", "success", "message", "Verification code sent to " + user.f_email);
    }

    // ----------------- Verify -----------------
    public Object verifyUser(String code) {
        ExpandoObj expandoObj = userRepoReg.findByVerificationCode(code);

        if (expandoObj == null) {
            return Map.of("status", "error", "message", "No user found with verification code: " + code);
        }

        if (!expandoObj.verified) {
            expandoObj.verified = true;
            expandoObj.verificationCode = "VERIFIED";
            userRepoReg.save(expandoObj);
            return Map.of("status", "success", "message", "User verified successfully: " + expandoObj.email);
        } else {
            return Map.of("status", "info", "message", "User already verified: " + expandoObj.email);
        }
    }

    // ----------------- Utility -----------------
    private String generateVerificationCode() {
        return String.format("%06d", random.nextInt(1_000_000));
    }
}
