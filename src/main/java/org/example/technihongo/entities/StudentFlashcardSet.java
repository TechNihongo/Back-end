package org.example.technihongo.entities;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "StudentFlashcardSet")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentFlashcardSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_set_id")
    private Integer studentSetId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "creator_id", referencedColumnName = "student_id", nullable = false)
    private Student creator;

    @ManyToOne
    @JoinColumn(name = "resource_id", referencedColumnName = "resource_id")
    private LearningResource learningResource;

    @Column(name = "total_cards")
    private Integer totalCard;

    @Column(name = "total_views")
    private Integer totalView;

    @Column(name = "is_public")
    private boolean isPublic = true;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "studentFlashCardSet", cascade = CascadeType.ALL)
    private Set<Flashcard> flashcards = new HashSet<>();

    @OneToMany(mappedBy = "studentFlashcardSet", cascade = CascadeType.ALL)
    private Set<StudentFlashcardSetProgress> progresses = new HashSet<>();
}