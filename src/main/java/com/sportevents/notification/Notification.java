package com.sportevents.notification;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;
    private String title;
    private String content;
    private Date date;
    private boolean seen;
    private Long eventId;
    @Column(name = "user_id")
    private Long userId;

    public Notification(String title, String content) {
        this.title = title;
        this.content = content;
        this.date = new Date();
        this.seen = false;
    }
}
