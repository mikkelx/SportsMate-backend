package com.sportevents.sport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) //jackson does not know how to serailze lazy loaded fields
public class Sport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long sportId;
    private String sportName;

    @ElementCollection
    @CollectionTable(name="sport_attributes", joinColumns=@JoinColumn(name="sportId"))
    private Set<String> attributes = new HashSet<>();

    public Sport(String sportName) {
        this.sportName = sportName;
    }

    public void addAttribute(String attribute) {
        this.attributes.add(attribute);
    }
}