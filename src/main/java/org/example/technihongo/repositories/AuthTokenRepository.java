package org.example.technihongo.repositories;

import org.example.technihongo.entities.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthTokenRepository extends JpaRepository<AuthToken, Integer> {
    AuthToken findByToken(String token);
    List<AuthToken> findAllByUser_UserIdAndTokenType(Integer userId, String tokenType);
    Optional<AuthToken> findByTokenAndTokenTypeAndIsActive(String token, String tokenType, boolean isActive);
}


