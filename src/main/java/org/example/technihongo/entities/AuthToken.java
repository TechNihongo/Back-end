package org.example.technihongo.entities;

import jakarta.persistence.*;
import lombok.*;
import org.example.technihongo.enums.TokenType;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "AuthToken")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Integer tokenId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Column(name = "token")
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(name = "token_type", length = 50)
    private TokenType tokenType;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createAt;


    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;
}