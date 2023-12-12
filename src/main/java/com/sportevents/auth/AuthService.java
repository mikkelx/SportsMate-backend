package com.sportevents.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.sportevents.common.UserRole;
import com.sportevents.exception.NotFoundException;
import com.sportevents.notification.NotificationService;
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
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthService {

    private FirebaseAuth firebaseAuth;
    private UserRepository userRepository;
    private NotificationService notificationService;

    @Autowired
    public AuthService(FirebaseAuth firebaseAuth, UserRepository userRepository, NotificationService notificationService) {
        this.firebaseAuth = firebaseAuth;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    public static Long getCurrentUserId() {
        Long uid = Long.parseLong((String)SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return uid;
    }

    public static String getCurrentUserRole() {
        String role = (String)SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().collect(Collectors.toList()).get(0).toString();
        return role;
    }

    public static boolean isAdmin() {
        return getCurrentUserRole().equals("ADMIN");
    }

    public ResponseEntity<String> register(RegisterRequest registerRequest) {
        if(!isRegisterRequestValid(registerRequest)) {
            return ResponseEntity.badRequest().body("Wprowadź wszystkie dane");
        }

        if(!isEmailValid(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Niepoprawny adres email");
        }

        if(checkIfUserExistsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.status(409).body("Użytkownik o adresie " + registerRequest.getEmail() + " email już istnieje");
        }

        if(checkIfUserExistsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.status(409).body("Użytkownik o nazwie " + registerRequest.getUsername() + " już istnieje");
        }

        if(!registerRequest.getPassword().equals(registerRequest.getPasswordRepeated())) {
            return ResponseEntity.badRequest().body("Hasła nie są takie same");
        }

        if(!verifyPassword(registerRequest.getPassword(), registerRequest.getPasswordRepeated())) {
            return ResponseEntity.badRequest().body("Hasło nie spełnia wymagań, jest za słabe");
        }
        

        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setUsername(registerRequest.getUsername());
        user.setLocked(false);
        user.setRole(UserRole.USER);
        user.resetLocation();
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
            this.setLockedClaim(createdUser.getUid(), false);
            return ResponseEntity.status(201).body("Użytkownik został zarejestrowany");
        } catch (FirebaseAuthException e) {
            userRepository.delete(user);
            this.deleteUserFromFirebase(user.getUserId());
            log.warn("Error registering user with email: " + registerRequest.getEmail());
            log.warn(e.toString());
            return ResponseEntity.internalServerError().body("Błąd podczas rejestracji użytkownika");
        }
    }

    private boolean checkIfUserExistsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    private boolean isEmailValid(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);

        return pattern.matcher(email).matches();
    }

    private void deleteUserFromFirebase(Long userId) {
        try {
            firebaseAuth.deleteUser(userId.toString());
        } catch (FirebaseAuthException e) {
            log.warn("Error deleting user with id: " + userId);
            log.warn(e.toString());
        }
    }

    private boolean checkIfUserExistsByEmail(String email) {
        try {
            if(firebaseAuth.getUserByEmail(email) != null) {
                return true;
            }
        } catch (FirebaseAuthException e) {
            return false;
        }
        return false;
    }

    private void setUserRole(String userId, String role) throws FirebaseAuthException {
        Map<String, Object> claims = this.getUserClaims(userId);
        claims.put("role", role);
        firebaseAuth.setCustomUserClaims(userId, claims);
    }

    private void setLockedClaim(String userId, boolean locked) throws FirebaseAuthException {
        Map<String, Object> claims = this.getUserClaims(userId);
        claims.put("locked", locked);
        firebaseAuth.setCustomUserClaims(userId, claims);
    }

    private Map<String, Object> getUserClaims(String userId) throws FirebaseAuthException {
        Map<String, Object> firebaseClaims = firebaseAuth.getUser(userId).getCustomClaims();
        if(firebaseClaims.isEmpty())
            return new HashMap<>();
        else return new HashMap<>(firebaseClaims);
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

        if (request.getUsername() == null || request.getUsername().isEmpty()) {
            return false;
        }

        return true;
    }

    public void grantAdminAccess(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));

        try {
            this.setUserRole(user.getUserId().toString(), "ADMIN");
            user.setRole(UserRole.ADMIN);
            userRepository.save(user);
            notificationService.notifyUserOfNewRole(userId, "ADMIN");
        } catch (FirebaseAuthException e) {
            throw new RuntimeException(e);
        }

    }

    public void grantUserAccess(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));

        try {
            this.setUserRole(user.getUserId().toString(), "USER");
            user.setRole(UserRole.USER);
            userRepository.save(user);
            notificationService.notifyUserOfNewRole(userId, "USER");
        } catch (FirebaseAuthException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<?> blockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        try {
            this.setLockedClaim(user.getUserId().toString(), true);
            notificationService.notifyUserOfAccountBan(userId);
            user.setLocked(true);
            userRepository.save(user);
            return ResponseEntity.ok().body("User was blocked");
        } catch (FirebaseAuthException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<?> unblockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        try {
            this.setLockedClaim(user.getUserId().toString(), false);
            user.setLocked(false);
            userRepository.save(user);
            return ResponseEntity.ok().body("User was unblocked");
        } catch (FirebaseAuthException e) {
            throw new RuntimeException(e);
        }
    }
}
