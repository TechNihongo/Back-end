package org.example.technihongo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "[UserActivityLog]")
public class UserActivityLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Integer logId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "activity_type")
    private String activityType;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "content_id")
    private Integer contentId;

    @Column(name = "description")
    private String description;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
}
