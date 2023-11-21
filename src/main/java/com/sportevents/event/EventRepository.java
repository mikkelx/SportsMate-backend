package com.sportevents.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByActive(boolean active);



//    @Query("SELECT a FROM Event a WHERE TYPE(a.sport) = :dtype")
//    List<Event> findAllEventsBySport(@Param("dtype") Class<?> dtype);



//    @Query("SELECT u FROM Event u JOIN Sport oe ON u.sport.sportId = oe.sportId WHERE oe.dtype = :dtype")
//    List<Event> findAllEventsBySport(@Param("dtype") String dtype);

//    @Query("SELECT a FROM Event a WHERE TYPE(a) = Running AND a.sport = :dtype")
//    List<Event> findAllEvents_Running(@Param("dtype") String dtype);
}
