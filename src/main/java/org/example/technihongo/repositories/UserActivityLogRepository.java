package org.example.technihongo.repositories;

import org.example.technihongo.entities.UserActivityLog;
import org.example.technihongo.enums.ActivityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserActivityLogRepository extends JpaRepository<UserActivityLog, Integer> {
    Page<UserActivityLog> findByUser_UserId(Integer userId, Pageable pageable);
    Page<UserActivityLog> findByUser_UserIdAndActivityTypeIn(Integer userId, List<ActivityType> allowedTypes, Pageable pageable);
}
