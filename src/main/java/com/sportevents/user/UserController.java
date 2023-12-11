package com.sportevents.user;

import com.google.api.gax.rpc.UnauthenticatedException;
import com.sportevents.auth.AuthService;
import com.sportevents.dto.Message;
import com.sportevents.exception.UnauthorizedException;
import com.sportevents.location.Location;
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
        if(AuthService.getCurrentUserRole() != "ADMIN")
            throw new UnauthorizedException("You are not authorized to perform this action");
        User user = userService.getUser(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/me")
    public ResponseEntity<User> getMe() {
        User user = userService.getUser(AuthService.getCurrentUserId());
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

    @PutMapping("/location")
    public ResponseEntity<?> setLocation(@RequestBody Location userLastLocation) {
        userService.setLocation(userLastLocation);
        return ResponseEntity.ok().body("Location was set");
    }

    @GetMapping("/location")
    public ResponseEntity<?> getLocation() {
        return ResponseEntity.ok().body(userService.getLocation());
    }

    @GetMapping("/event/created")
    public ResponseEntity<?> getEventsCreatedByUser() {
        return ResponseEntity.ok().body(userService.getEventsCreatedByUser());
    }


    @GetMapping("/sport/preference")
    public ResponseEntity<?> getSportPreference() {
        return ResponseEntity.ok().body(userService.getSportPreference());
    }

    @PutMapping("/sport/preference")
    public ResponseEntity<?> setSportPreference(@RequestParam Long sportId) {
        userService.setSportPreference(sportId);
        return ResponseEntity.ok().body("Sport preference was set");
    }

    @DeleteMapping("/sport/preference")
    public ResponseEntity<?> deleteSportPreference(@RequestParam Long sportId) {
        userService.deleteSportPreference(sportId);
        return ResponseEntity.ok().body("Sport preference was deleted");
    }

    @DeleteMapping("/sport/preference/all")
    public ResponseEntity<?> deleteAllSportPreferences() {
        userService.deleteAllSportPreferences();
        return ResponseEntity.ok().body("Sport preferences were deleted");
    }

}
