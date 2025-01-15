package org.example.technihongo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "[LearningPath]")
public class LearningPath {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "path_id")
    private Integer pathId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "domain_id")
    private Domain domain;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @Column(name = "is_public")
    private boolean isPublic;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
}
