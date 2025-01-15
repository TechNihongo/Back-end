package org.example.technihongo.entities;


import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "FlashCard")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentFlashCardSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "set_id")
    private Integer setId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "domain_id")
    private Domain domain;
    @ManyToOne
    @JoinColumn(name = "difficulty_level_id")
    private DifficultyLevel difficultyLevel;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private Student creator;

    @ManyToOne
    @JoinColumn(name = "resource_id")
    private LearningResource learningResource;

    @Column(name = "is_public")
    private boolean isPublic;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();


}
