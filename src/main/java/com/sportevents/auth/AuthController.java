package com.sportevents.auth;

import com.sportevents.request.RegisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class AuthController {

    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest)  {
        return authService.register(registerRequest);
    }

    @GetMapping("/access/admin")
    public ResponseEntity<?> grantAdminAccess(@RequestParam Long userId) {
        authService.grantAdminAccess(userId);
        return ResponseEntity.ok().body("Admin access was granted");
    }

    @GetMapping("/access/user")
    public ResponseEntity<?> grantUserAccess(@RequestParam Long userId) {
        authService.grantUserAccess(userId);
        return ResponseEntity.ok().body("User access was granted");
    }

}
