package org.example.technihongo.entities;


import java.time.LocalDateTime;

import org.antlr.v4.runtime.misc.NotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Course")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Integer courseId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @ManyToOne
    @JoinColumn(name = "domain_id")
    private Domain domain;

    @ManyToOne
    @JoinColumn(name = "difficulty_level_id")
    private DifficultyLevel difficultyLevel;

    @Column(name = "attachment_url")
    private String attachmentUrl;

    @Column(name = "estimated_duration")
    private Integer estimatedDuration;

    @Column(name = "is_public")
    @Builder.Default
    private boolean isPublic = false;


    @Column(name = "is_premium")
    @Builder.Default
    private boolean isPremium = false;

    @SuppressWarnings("deprecation")
    @Column(name = "created_at")
    @NotNull
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();


    



}
