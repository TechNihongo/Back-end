package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.UserActivityLogDTO;
import org.example.technihongo.enums.ActivityType;
import org.example.technihongo.enums.ContentType;

import java.util.List;

public interface UserActivityLogService {
    void trackUserActivityLog(Integer userId, ActivityType activityType, ContentType contentType,
                              Integer contentId, String ipAddress, String userAgent);
    List<UserActivityLogDTO> getUserActivityLogs(Integer userId, int page, int size);
    List<UserActivityLogDTO> getStudentActivityLogs(Integer userId, int page, int size);
}
