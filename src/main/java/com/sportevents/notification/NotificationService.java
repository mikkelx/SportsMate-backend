package com.sportevents.notification;

import com.sportevents.auth.AuthService;
import com.sportevents.exception.NotFoundException;
import com.sportevents.user.User;
import com.sportevents.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.List;

@Service
public class NotificationService {

    private NotificationRepository notificationRepository;
    private UserRepository userRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> createNotification(Notification notification) {
        notification.setUserId(AuthService.getCurrentUserId());
        return ResponseEntity.ok().body(notificationRepository.save(notification));
    }

    public ResponseEntity<?> createNotification(String title, String content) {
        Notification notification = new Notification(title, content);
        notification.setUserId(AuthService.getCurrentUserId());
        return ResponseEntity.ok().body(notificationRepository.save(notification));
    }

    public ResponseEntity<?> getNotifications() {
        return ResponseEntity.ok().body(notificationRepository.findAllByUserIdAndDateAfter(
                AuthService.getCurrentUserId(),
                Date.from(new Date().toInstant().plus(Duration.ofDays(-14))) //get only notifications from the last 14 days
                ));

    }

    public ResponseEntity<?> getExpiredNotifications() {
        return ResponseEntity.ok().body(notificationRepository.findAllByUserId(AuthService.getCurrentUserId()));

    }

    public ResponseEntity<?> readNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(
                () -> new NotFoundException("Notification was not found!"));
        notification.setSeen(true);
        return ResponseEntity.ok().body(notificationRepository.save(notification));
    }

    public void notifyUsersOfNewEvent(Long sportId) {
        List<User> users = userRepository.findBySportPreferences(List.of(sportId));
        for (User user : users) {
            createNotification("New event", "There is a new event that you may be interested in!");
        }
    }

}
