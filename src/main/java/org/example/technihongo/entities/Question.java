package org.example.technihongo.entities;

import jakarta.persistence.*;
import lombok.*;
import org.example.technihongo.enums.QuestionType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "Question")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Integer questionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", length = 50)
    private QuestionType questionType;

    @Column(name = "question_text")
    private String questionText;

    @Column(name = "explanation")
    private String explanation;

    @Column(name = "attachment_url")
    private String url;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
