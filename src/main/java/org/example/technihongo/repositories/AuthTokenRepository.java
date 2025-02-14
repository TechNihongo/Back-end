package org.example.technihongo.repositories;

import org.example.technihongo.entities.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthTokenRepository extends JpaRepository<AuthToken, Integer> {
    AuthToken findByTokenId(Integer tokenId);
    AuthToken findByToken(String token);
}
