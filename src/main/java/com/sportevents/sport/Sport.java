package com.sportevents.sport;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.firebase.database.annotations.NotNull;
import com.sportevents.sport.subclasses.Cycling;
import com.sportevents.sport.subclasses.Football;
import com.sportevents.sport.subclasses.Running;
import jakarta.persistence.*;
import lombok.*;

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
