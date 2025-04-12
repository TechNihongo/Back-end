package org.example.technihongo.repositories;

import org.example.technihongo.entities.AuthToken;
import org.example.technihongo.enums.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AuthTokenRepository extends JpaRepository<AuthToken, Integer> {
    AuthToken findByToken(String token);
    List<AuthToken> findAllByUser_UserIdAndTokenType(Integer user_userId, TokenType tokenType);
    Optional<AuthToken> findByTokenAndTokenTypeAndIsActive(String token, TokenType tokenType, Boolean isActive);

    @Transactional
    @Modifying
    @Query("DELETE FROM AuthToken t WHERE t.expiresAt <= :expiryTime")
    void deleteExpiredTokens(@Param("expiryTime") LocalDateTime expiryTime);

    List<AuthToken> findAllByUser_UserIdAndTokenTypeAndIsActiveOrderByCreatedAtAsc(Integer userId, TokenType tokenType, boolean isActive);
}



