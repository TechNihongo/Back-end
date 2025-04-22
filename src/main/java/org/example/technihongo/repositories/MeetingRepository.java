package org.example.technihongo.repositories;

import org.example.technihongo.entities.Meeting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Integer> {
    @Query("SELECT m FROM Meeting m WHERE m.isActive = true")
    Page<Meeting> findActiveMeetings(Pageable pageable);

    @Query("SELECT m FROM Meeting m WHERE m.meetingId = :meetingId AND m.isActive = true")
    Optional<Meeting> findActiveMeetingById(@Param("meetingId") Integer meetingId);
}
