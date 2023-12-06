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

    @PutMapping("/unblock")
    public ResponseEntity<?> unblockUser(@RequestParam Long userId) {
        User user = userService.unblockUser(userId);
        return ResponseEntity.ok().body(user);
    }

    @PutMapping("/sport/preference")
    public ResponseEntity<?> setSportPreference(@RequestParam Long sportId) {
        userService.setSportPreference(sportId);
        return ResponseEntity.ok().body("Zapisano preferencje sportowe");
    }

    @DeleteMapping("/sport/preference")
    public ResponseEntity<?> deleteSportPreference(@RequestParam Long sportId) {
        userService.deleteSportPreference(sportId);
        return ResponseEntity.ok().body("Usunięto preferencję sportową");
    }

    @DeleteMapping("/sport/preference/all")
    public ResponseEntity<?> deleteAllSportPreferences() {
        userService.deleteAllSportPreferences();
        return ResponseEntity.ok().body("Usunięto wszystkie preferencje sportowe");
    }

}
