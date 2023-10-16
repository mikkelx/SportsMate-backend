package com.sportevents.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
        List<User> findUsersByJoinedEvents_eventId(Long eventId);
        boolean existsUserByJoinedEvents_eventID(Long eventId);
}
