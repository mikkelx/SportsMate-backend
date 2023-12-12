package com.sportevents.notification;

import com.sportevents.auth.AuthService;
import com.sportevents.event.Event;
import com.sportevents.exception.NotFoundException;
import com.sportevents.location.Location;
import com.sportevents.user.User;
import com.sportevents.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.sportevents.event.EventService.calculateDistance;
import static com.sportevents.specifications.UserSpecs.notificationFilter;

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

    public ResponseEntity<?> getNotificationHistory() {
        return ResponseEntity.ok().body(notificationRepository.findAllByUserId(AuthService.getCurrentUserId()));

    }

    public ResponseEntity<?> readNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(
                () -> new NotFoundException("Notification was not found!"));
        notification.setSeen(true);
        return ResponseEntity.ok().body(notificationRepository.save(notification));
    }

    public void notifyUsersOfNewEvent(Event event) {
        List<User> users = userRepository.findAll(notificationFilter(event.getLocation(), event.getSport().getSportId()));

        users.stream().filter(
                user -> calculateDistance(new Location(user.getLastLat(), user.getLastLng()), event.getLocation()) < user.getRangePreference() &&
                        !Objects.equals(user.getUserId(), event.getOrganizerId())
        ).forEach(user -> {
            Notification notification = new Notification("New event near you!",
                    "There is a new event near you!");
            notification.setUserId(user.getUserId());
            notification.setEventId(event.getEventId());
            notificationRepository.save(notification);
            }
        );
    }

    public void notifyUserOfDeletedComment(Long userId, Long eventId) {
        Notification notification = new Notification("Comment deleted!",
                "Your comment was deleted by the administrator!");
        notification.setUserId(userId);
        notification.setEventId(eventId);
        notificationRepository.save(notification);
    }

    public void notifyUserOfEventUpdate(Long userId, Long eventId) {
        Notification notification = new Notification("Event update!",
                "Event you are participating in was updated!");
        notification.setUserId(userId);
        notification.setEventId(eventId);
        notificationRepository.save(notification);
    }

    public void notifyUserOfEventCancellation(Long userId, Long eventId) {
        Notification notification = new Notification("Event cancelled!",
                "Event you are participating in was cancelled!");
        notification.setUserId(userId);
        notification.setEventId(eventId);
        notificationRepository.save(notification);
    }

    public void notifyUserOfAccountBan(Long userId) {
        Notification notification = new Notification("Account banned!",
                "Your account was locked by the administrator!");
        notification.setUserId(userId);
        notificationRepository.save(notification);
    }

}
