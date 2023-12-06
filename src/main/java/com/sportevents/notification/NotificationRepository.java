package com.sportevents.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByUserId(Long userId);
    List<Notification> findAllByUserIdAndDateAfter(Long userId, Date date);
    List<Notification> findAllByUserIdAndSeen(Long userId, boolean seen);

}
