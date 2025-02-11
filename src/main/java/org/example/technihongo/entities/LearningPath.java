package org.example.technihongo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "LearningPath")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LearningPath {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "path_id")
    private Integer pathId;

    @NotNull
    @Size(max = 50)
    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Size(max = 255)
    @Column(name = "description", length = 255)
    private String description;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "domain_id", nullable = false, referencedColumnName = "domain_id")
    private Domain domain;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false, referencedColumnName = "user_id")
    private User creator;

    @Column(name = "total_courses", nullable = false)
    private Integer totalCourses;

    @Builder.Default
    @Column(name = "is_public", nullable = false)
    private boolean isPublic = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;
}