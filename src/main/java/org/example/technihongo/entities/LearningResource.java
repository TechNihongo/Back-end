package org.example.technihongo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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
    @NotNull
    @JoinColumn(name = "domain_id", referencedColumnName = "domain_id", nullable = false)
    private Domain domain;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "creator_id", referencedColumnName = "user_id", nullable = false)
    private User creator;

    @Column(name = "is_public")
    private boolean isPublic;

    @Column(name = "is_premium")
    private boolean isPremium;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "video_filename", length = 100)
    private String videoFilename;

    @Column(name = "pdf_url")
    private String pdfUrl;

    @Column(name = "pdf_filename", length = 100)
    private String pdfFilename;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}