package com.sportevents.specifications;

import com.sportevents.event.Event;
import com.sportevents.location.Location;
import com.sportevents.request.FilterCriteria;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class EventSpecs {

    public static Specification<Event> mainFilter(FilterCriteria filterCriteria) {
        return((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(filterCriteria.getDateStart() != null && filterCriteria.getDateEnd() != null) {
                predicates.add(criteriaBuilder.between(root.get("date"),filterCriteria.getDateStart(),filterCriteria.getDateEnd()));
            }

            if(filterCriteria.getDateStart() != null) {
                predicates.add(criteriaBuilder.greaterThan(root.get("date"),filterCriteria.getDateStart()));
            }

            if(filterCriteria.getDateEnd() != null) {
                predicates.add(criteriaBuilder.lessThan(root.get("date"),filterCriteria.getDateEnd()));
            }

            if(filterCriteria.getSportLevel() != null) {
                predicates.add(criteriaBuilder.equal(root.get("sportLevel"),filterCriteria.getSportLevel()));
            }

            if (filterCriteria.isCyclical()) {
                predicates.add(criteriaBuilder.equal(root.get("isCyclical"), filterCriteria.isCyclical()));
            }

            if(filterCriteria.getSport() != null && !filterCriteria.getSport().isEmpty()) {
                predicates.add(root.get("sport").in(filterCriteria.getSport()));
            }

            if(filterCriteria.getParticipantsNumberStart() != 0 && filterCriteria.getParticipantsNumberEnd() != 0) {
                predicates.add(criteriaBuilder.between(root.get(
                        "participantsNumber"),
                        filterCriteria.getParticipantsNumberStart(),
                        filterCriteria.getParticipantsNumberEnd()));
            }

            if(filterCriteria.getParticipantsNumberStart() != 0) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("participantsNumber"),
                        filterCriteria.getParticipantsNumberStart()));
            }

            if(filterCriteria.getParticipantsNumberEnd() != 0) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("participantsNumber"),
                        filterCriteria.getParticipantsNumberEnd()));
            }

            //basic filter - get only events in range of about 10km
            if(filterCriteria.getUserLocation() != null ) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("location").get("lat"),filterCriteria.getUserLocation().getLat() + 0.1));
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("location").get("lat"),filterCriteria.getUserLocation().getLat() - 0.1));
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("location").get("lng"),filterCriteria.getUserLocation().getLng() + 0.15));
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("location").get("lng"),filterCriteria.getUserLocation().getLng() - 0.15));
            }

            //always get active events
            predicates.add(criteriaBuilder.equal(root.get("active"), true));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

}
