package org.example.technihongo.entities;

import jakarta.persistence.*;
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
@Table(name = "[DifficultyLevel]")
public class DifficultyLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "level_id")
    private Integer levelId;

    @Enumerated(EnumType.STRING)
    @Column(name ="tag", unique = true)
    private LevelTag tag;

    @Column(name = "name")
    private String name;

    @Size(max = 255)
    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "order_sequence")
    private Integer orderSequence;

    @Column(name = "is_active")
    @Builder.Default
    private boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum LevelTag{
        N5,N4,N3,N2,N1
    }
}
