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
@Table(name = "[LearningResource]")
public class LearningResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resource_id")
    private Integer resourceId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "resource_type_id")
    private ResourceType resourceType;

    @Column(name = "content_url")
    private String contentUrl;

    @ManyToOne
    @JoinColumn(name = "domain_id")
    private Domain domain;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @Column(name = "is_public")
    private boolean isPublic;

    @Column(name = "is_premium")
    private boolean isPremium;


    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

}
