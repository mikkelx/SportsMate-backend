//package com.sportevents.sport;
//
//import com.sportevents.sport.subclasses.SportAttribute;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
////@JsonTypeInfo(
////        use = JsonTypeInfo.Id.NAME,
////        include = JsonTypeInfo.As.PROPERTY,
////        property = "dtype")
////@JsonSubTypes({
////        @JsonSubTypes.Type(value = Football.class, name = "Football"),
////        @JsonSubTypes.Type(value = Running.class, name = "Running"),
////        @JsonSubTypes.Type(value = Cycling.class, name = "Cycling")
////})
//@Entity
//@Table(name = "sports") // specify unique table name
//public class Sport {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    protected Long sportId;
//    private String sportName;
//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<SportAttribute> attributes;
//    public Sport(String sportName) {
//        this.sportName = sportName;
//        this.attributes = new ArrayList<>();
//    }
//    public void addAttribute(String name, String value) {
//        SportAttribute sportAttribute = new SportAttribute(name, value);
//        this.attributes.add(sportAttribute);
//    }
//
//    public void addAttribute(SportAttribute sportAttribute) {
//        this.attributes.add(sportAttribute);
//    }
//}
