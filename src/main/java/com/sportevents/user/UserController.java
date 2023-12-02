package com.sportevents.user;

import com.sportevents.auth.AuthService;
import com.sportevents.dto.Message;
import com.sportevents.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<User> getUser(@RequestParam Long userId) {
        User user = userService.getUser(userId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/block")
    public ResponseEntity<?> blockUser(@RequestParam Long userId) {
        User user = userService.blockUser(userId);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/id")
    public ResponseEntity<?> getMyId() {
        return ResponseEntity.ok().body(new Message(AuthService.getCurrentUserId().toString()));
    }

    @GetMapping("/role")
    public ResponseEntity<?> getMyRole() {
        return ResponseEntity.ok().body(new Message(AuthService.getCurrentUserRole()));
    }

}
