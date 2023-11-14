package com.sportevents.event;

import com.google.firebase.database.annotations.NotNull;
import com.sportevents.location.Location;
import com.sportevents.sport.subclasses.SportAttribute;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity
@ToString
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;
    @NotNull
    private String title;
    @NotNull
    private Date date;
    private String description;
    private boolean active;
    private Long organizerId;
    private String sportName;
    @Transient
    private int participantsNumber;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "event_attributes",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "attribute_id"))
    private List<SportAttribute> attributes;

    @ManyToOne(fetch = FetchType.EAGER)
    private Location location;

    public void addAttribute(String name, String value) {
        this.attributes.add(new SportAttribute(name, value));
    }

    public void increaseParticipantsNumber() {
        this.participantsNumber++;
    }

}
