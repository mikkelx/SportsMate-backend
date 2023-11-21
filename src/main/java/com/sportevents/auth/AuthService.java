package com.sportevents.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.sportevents.request.RegisterRequest;
import com.sportevents.user.User;
import com.sportevents.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Service
@Slf4j
public class AuthService {

    private FirebaseAuth firebaseAuth;
    private UserRepository userRepository;

    @Autowired
    public AuthService(FirebaseAuth firebaseAuth, UserRepository userRepository) {
        this.firebaseAuth = firebaseAuth;
        this.userRepository = userRepository;
    }

    public static Long getCurrentUserId() {
        Long uid = Long.parseLong((String)SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return uid;
    }

    public ResponseEntity<String> register(RegisterRequest registerRequest) {
        if(!isRegisterRequestValid(registerRequest)) {
            return ResponseEntity.badRequest().body("Register request is invalid");
        }

        if(checkIfUserExists(registerRequest.getEmail())) { //409 resource conflict
            return ResponseEntity.status(409).body("User with email: " + registerRequest.getEmail() + " is already registered!");
        }

        if(!verifyPassword(registerRequest.getPassword(), registerRequest.getPasswordRepeated())) {
            return ResponseEntity.badRequest().body("Password is not matching conditions");
        }

        if(!isEmailValid(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Email is not matching requirements");
        }

        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setUsername(registerRequest.getUsername());
        user = userRepository.save(user);

        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(registerRequest.getEmail())
                .setEmailVerified(false)
                .setPassword(registerRequest.getPassword())
                .setDisplayName(registerRequest.getUsername())
                .setUid(user.getUserId().toString());

        try{
            UserRecord createdUser = firebaseAuth.createUser(request);
            this.setUserRole(createdUser.getUid(), "USER");
            return ResponseEntity.status(201).body("User created with id: " + createdUser.getUid());
        } catch (FirebaseAuthException e) {
            userRepository.delete(user);
            log.warn("Error registering user with email: " + registerRequest.getEmail());
            log.warn(e.toString());
            return ResponseEntity.internalServerError().body("User with email" + registerRequest.getEmail() + " not registered");
        }
    }

    private boolean isEmailValid(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);

        return pattern.matcher(email).matches();
    }

    private boolean checkIfUserExists(String email) {
        try {
            if(firebaseAuth.getUserByEmail(email) != null) {
                return true;
            }
        } catch (FirebaseAuthException e) {
            return false;
        }
        return false;
    }

    private void setUserRole(String id, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        try {
            firebaseAuth.setCustomUserClaims(id, claims);
        } catch (FirebaseAuthException e) {
            log.error("Error while assigning user role");
        }
    }

    private boolean verifyPassword(String password, String repeatedPassword) {
        if(!password.equals(repeatedPassword))
            return false;

        if (password.length() < 8 || password.length() > 25) {
            return false;
        }

        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowercase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            }

            if (hasUppercase && hasLowercase && hasDigit) {
                break;
            }
        }

        return hasUppercase && hasLowercase && hasDigit;
    }

    private boolean isRegisterRequestValid(RegisterRequest request) {
        if (request == null)
            return false;

        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            return false;
        }

        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            return false;
        }

        if (request.getPasswordRepeated() == null || request.getPasswordRepeated().isEmpty()) {
            return false;
        }

        if (!request.getPassword().equals(request.getPasswordRepeated())) {
            return false;
        }

        if (request.getUsername() == null || request.getUsername().isEmpty()) {
            return false;
        }

        return true;
    }

}
