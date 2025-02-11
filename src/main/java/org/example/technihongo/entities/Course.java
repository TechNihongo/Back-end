package org.example.technihongo.entities;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "[Course]")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Integer courseId;

    @NotNull
    @Size(max = 50)
    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Size(max = 255)
    @Column(name = "description", length = 255)
    private String description;

    @ManyToOne
    @JoinColumn(name = "creator_id", referencedColumnName = "user_id")
    private User creator;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "domain_id", referencedColumnName = "domain_id", nullable = false)
    private Domain domain;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "difficulty_level_id", referencedColumnName = "level_id", nullable = false)
    private DifficultyLevel difficultyLevel;

    @Column(name = "attachment_url")
    private String attachmentUrl;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "estimated_duration")
    private String estimatedDuration;

    @Column(name = "is_public")
    @Builder.Default
    private boolean isPublic = true;

    @Column(name = "is_premium")
    @Builder.Default
    private boolean isPremium = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

}
