package org.example.technihongo.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "FlashCard")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlashCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vocab_id")
    private Integer vocabId;

    @ManyToOne
    @JoinColumn(name = "set_id")
    private StudentFlashCardSet studentFlashCardSet;

    @Column(name = "japanese_definition")
    private String definition;

    @Column(name = "viet_eng_translation")
    private String translation;

    @Column(name = "image_url")
    private String imgUrl;

    @Column(name = "vocab_order")
    private Integer vocabOrder;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "update_At")
    @Builder.Default
    private LocalDateTime updateAt = LocalDateTime.now();
}
