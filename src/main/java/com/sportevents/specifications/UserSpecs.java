package com.sportevents.specifications;

import com.sportevents.location.Location;
import com.sportevents.user.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecs {

    public static Specification<User> notificationFilter(Location eventLocation, Long sportId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            //basic filter - get only users with last location only about 10km from event
            if(eventLocation != null && eventLocation.getLng()!= 0 && eventLocation.getLat() != 0) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("lastLat"), eventLocation.getLat() + 0.1));
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("lastLat"), eventLocation.getLat() - 0.1));
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("lastLng"), eventLocation.getLng() + 0.15));
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("lastLng"), eventLocation.getLng() - 0.15));
            }

            //get only users with this sport in preferences
            if(sportId != null && sportId > 0) {
                predicates.add(criteriaBuilder.isMember(sportId, root.get("sportPreferences")));
            }

            predicates.add(criteriaBuilder.equal(root.get("locked"), false));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
