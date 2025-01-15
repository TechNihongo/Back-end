package org.example.technihongo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "AuthToken")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Integer tokenId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "token")
    private String token;

    @Column(name = "tokenType")
    private String tokenType;

    @Column(name = "expiresAt")
    private LocalDateTime expiresAt;

    @Column(name = "createAt")
    @Builder.Default
    private LocalDateTime createAt = LocalDateTime.now();


    @Column(name = "isActive")
    @Builder.Default
    private Boolean isActive = true;
}
