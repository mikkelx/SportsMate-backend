package com.sportevents.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
//    List<User> findAllByJoinedEvents(Long eventId);
//        List<User> findUsersByJoinedEvents(Long eventId);
//        List<User> findUsers_eventId(Long eventId);
//        List<User> findAllByJoinedEventsContaining(Long eventId);
        List<User> findUsersByJoinedEvents_eventId(Long eventId);
}
