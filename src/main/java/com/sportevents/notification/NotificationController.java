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

    @GetMapping("/history")
    public ResponseEntity<?> getNotificationHistory() {
        return notificationService.getNotificationHistory();
    }

    @PutMapping("/read")
    public ResponseEntity<?> readNotification(@RequestParam Long notificationId) {
        return notificationService.readNotification(notificationId);
    }

}
