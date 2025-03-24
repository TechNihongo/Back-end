package org.example.technihongo.entities;

import jakarta.persistence.*;
import lombok.*;
import org.example.technihongo.enums.ActivityType;
import org.example.technihongo.enums.ContentType;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "[UserActivityLog]")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Integer logId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity_type", length = 100, nullable = false)
    private ActivityType activityType;

    @Enumerated(EnumType.STRING)
    @Column(name = "content_type", length = 50)
    private ContentType contentType;

    @Column(name = "content_id")
    private Integer contentId;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;
}
