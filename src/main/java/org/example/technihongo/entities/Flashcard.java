package org.example.technihongo.entities;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Flashcard")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Flashcard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flashcard_id")
    private Integer flashCardId;

    @ManyToOne
    @JoinColumn(name = "student_set_id")
    private StudentFlashcardSet studentFlashCardSet;

    @ManyToOne
    @JoinColumn(name = "system_set_id")
    private SystemFlashcardSet systemFlashCardSet;

    @Column(name = "japanese_definition")
    private String definition;

    @Column(name = "viet_eng_translation")
    private String translation;

    @Column(name = "image_url")
    private String imgUrl;

    @Column(name = "card_order")
    private Integer vocabOrder;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updateAt;
}