package org.example.technihongo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "SystemFlashcardSet")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemFlashcardSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "system_set_id")
    private Integer systemSetId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "creator_id", referencedColumnName = "user_id", nullable = false)
    private User creator;

//    @ManyToOne
//    @NotNull
//    @JoinColumn(name = "domain_id", referencedColumnName = "domain_id",nullable = false )
//    private Domain domain;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "difficulty_level_id", referencedColumnName = "level_id", nullable = false)
    private DifficultyLevel difficultyLevel;

    @Column(name = "is_public")
    private boolean isPublic;

    @Column(name = "is_premium")
    private boolean isPremium;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @JsonIgnore
    @OneToMany(mappedBy = "systemFlashCardSet", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Flashcard> flashcards = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "systemFlashcardSet", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<StudentFlashcardSetProgress> progresses = new HashSet<>();
}
