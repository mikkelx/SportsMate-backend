package com.sportevents.specifications;

import com.sportevents.event.Event;
import com.sportevents.request.FilterCriteria;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventSpecs {

    public static Specification<Event> mainFilter(FilterCriteria filterCriteria) {
        return((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(filterCriteria.getDate() != null) {
                predicates.add(criteriaBuilder.greaterThan(root.get("date"),filterCriteria.getDate()));
            }

            if(filterCriteria.getSportLevel() != null) {
                predicates.add(criteriaBuilder.equal(root.get("sportLevel"),filterCriteria.getSportLevel()));
            }

            if(filterCriteria.getSport() != null) {
                predicates.add(criteriaBuilder.equal(root.get("sport"),filterCriteria.getSport()));
            }

            if(filterCriteria.getParticipantsNumberStart() != 0 && filterCriteria.getParticipantsNumberEnd() != 0) {
                predicates.add(criteriaBuilder.between(root.get("participantsNumber"),filterCriteria.getParticipantsNumberStart(),filterCriteria.getParticipantsNumberEnd()));
            }

            if(filterCriteria.getParticipantsNumberStart() != 0) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("participantsNumber"),filterCriteria.getParticipantsNumberStart()));
            }

            if(filterCriteria.getParticipantsNumberEnd() != 0) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("participantsNumber"),filterCriteria.getParticipantsNumberEnd()));
            }

            //basic filter - get active nad not that far away
            if(filterCriteria.getUserLocation() != null && predicates.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("active"), true));


                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("location").get("lat"),filterCriteria.getUserLocation().getLat() + 0.1));
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("location").get("lat"),filterCriteria.getUserLocation().getLat() - 0.1));
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("location").get("lng"),filterCriteria.getUserLocation().getLng() + 0.1));
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("location").get("lng"),filterCriteria.getUserLocation().getLng() - 0.1));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

    public static Specification<Event> isDateBefore(Date date) {
        return((root, query, criteriaBuilder) -> {
            return criteriaBuilder.lessThan(root.get("date"), date);
        });
    }

    public static Specification<Event> isDateBetween(Date date1, Date date2) {
        return((root, query, criteriaBuilder) -> {
            return criteriaBuilder.between(root.get("date"), date1, date2);
        });
    }

    public static Specification<Event> isSportLevel(String sportLevel) {
        return((root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("sportLevel"), sportLevel);
        });
    }

    public static Specification<Event> isSport_sportId(Long sportId) {
        return((root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("sport").get("sportId"), sportId);
        });
    }

}
