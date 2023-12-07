package com.sportevents.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<?> getNotifications() {
        return notificationService.getNotifications();
    }

    @GetMapping("/expired")
    public ResponseEntity<?> getExpiredNotifications() {
        return notificationService.getExpiredNotifications();
    }

    @PutMapping("/read")
    public ResponseEntity<?> readNotification(@RequestParam Long notificationId) {
        return notificationService.readNotification(notificationId);
    }

}
