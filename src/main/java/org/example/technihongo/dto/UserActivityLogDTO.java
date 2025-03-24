package org.example.technihongo.dto;

import lombok.*;
import org.example.technihongo.enums.ActivityType;
import org.example.technihongo.enums.ContentType;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserActivityLogDTO {
    private Integer logId;
    private ActivityType activityType;
    private ContentType contentType;
    private Integer contentId;
    private String description;
    private LocalDateTime createdAt;
}
