package com.sportevents.sport;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sportevents.sport.subclasses.Cycling;
import com.sportevents.sport.subclasses.Football;
import com.sportevents.sport.subclasses.Running;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "dtype")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Football.class, name = "Football"),
        @JsonSubTypes.Type(value = Running.class, name = "Running"),
        @JsonSubTypes.Type(value = Cycling.class, name = "Cycling")
})
public abstract class Sport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long sportId;
}
