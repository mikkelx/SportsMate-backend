package com.sportevents.sport.subclasses;

import com.sportevents.event.Event;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "attributes")
public class SportAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long attributeId;
    private String name;
    private String value;

    public SportAttribute(String name, String value) {
        this.name = name;
        this.value = value;
    }
}