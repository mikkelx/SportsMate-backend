package com.sportevents.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
        List<User> findUsersByJoinedEvents_eventId(Long eventId);

        @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u JOIN u.joinedEvents e WHERE u.userId = :userId AND e.id = :eventId")
        boolean existsByUserIdAndEventId(@Param("userId") Long userId, @Param("eventId") Long eventId);

        @Query("SELECT u FROM User u JOIN u.sportPreferences sp WHERE sp IN :preferences")
        List<User> findBySportPreferences(List<Long> preferences);

        boolean existsByUsername(String username);

}
