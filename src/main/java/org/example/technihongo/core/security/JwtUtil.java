package org.example.technihongo.core.security;

import org.example.technihongo.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTHelper jwtHelper;
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String secretKey;

    public int extractUserId(String token) {
        logger.info("Secret Key: {}", secretKey);
        logger.info("Token: {}", token);

        Claims claims = extractAllClaims(token);

        String email = jwtHelper.getEmailFromToken(token);
        int userId = userRepository.findByEmail(email).get().getUserId();

//        if (claims == null || claims.get("cid") == null) {
        if (claims == null) {
            logger.error("uid claim is missing or null");
            throw new IllegalArgumentException("Invalid token: uid claim is missing or null");
        }

        //return Integer.parseInt(claims.get("cid").toString());
        return userId;
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            logger.error("Failed to extract claims: {}", e.getMessage());
            throw e;
        }
    }
}
