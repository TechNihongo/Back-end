package org.example.technihongo.services.serviceimplements;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.CreateLoginTokenDTO;
import org.example.technihongo.dto.TokenStatusDTO;
import org.example.technihongo.entities.AuthToken;
import org.example.technihongo.entities.User;
import org.example.technihongo.enums.TokenType;
import org.example.technihongo.repositories.AuthTokenRepository;
import org.example.technihongo.repositories.UserRepository;
import org.example.technihongo.services.interfaces.AuthTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Component
public class AuthTokenServiceImpl implements AuthTokenService {
    @Autowired
    private AuthTokenRepository authTokenRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void saveLoginToken(CreateLoginTokenDTO createLoginTokenDTO) {
        authTokenRepository.save(AuthToken.builder()
                .user(userRepository.findByUserId(createLoginTokenDTO.getUserId()))
                .token(createLoginTokenDTO.getToken())
                .tokenType(TokenType.valueOf(createLoginTokenDTO.getTokenType()))
                .expiresAt(createLoginTokenDTO.getExpiresAt())
                .build());
    }

    @Override
    public void setTokenStatus(TokenStatusDTO tokenStatus) {
        AuthToken authToken = authTokenRepository.findByToken(tokenStatus.getToken());
        authToken.setIsActive(tokenStatus.isActive());
        authTokenRepository.save(authToken);
    }

    @Override
    public void updateLoginTokenStatus(Integer userId) {
        List<AuthToken> authTokenList = authTokenRepository.findAll().stream()
                .filter(authToken -> authToken.getUser().getUserId().equals(userId) &&
                        (authToken.getTokenType().equals(TokenType.LOGIN)
                        || authToken.getTokenType().equals(TokenType.LOGIN_GOOGLE)) &&
                        authToken.getIsActive())
                .toList();

        for(AuthToken a : authTokenList){
            if(a.getExpiresAt().isBefore(LocalDateTime.now())){
                a.setIsActive(false);
            }
        }

        authTokenRepository.saveAll(authTokenList);
    }

    @Override
    public String createEmailVerifyToken(Integer userId) {
        String token = UUID.randomUUID().toString();
        AuthToken authToken = new AuthToken();
        authToken.setUser(userRepository.findById(userId).get());
        authToken.setToken(token);
        authToken.setTokenType(TokenType.EMAIL_VERIFICATION);
        authToken.setExpiresAt(LocalDateTime.now().plusHours(24));
        authToken.setIsActive(true);
        authTokenRepository.save(authToken);
        return token;
    }

    @Override
    public void deactivateAllTokensByUserId(Integer userId, String tokenType) {
        List<AuthToken> tokens = authTokenRepository.findAllByUser_UserIdAndTokenType(userId, TokenType.valueOf(tokenType));
        for (AuthToken token : tokens) {
            token.setIsActive(false);
        }
        authTokenRepository.saveAll(tokens);
    }

    @Override
    public String resendVerificationEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found."));

        if (user.isVerified()) {
            throw new RuntimeException("Email is already verified.");
        }

        deactivateAllTokensByUserId(user.getUserId(), String.valueOf(TokenType.EMAIL_VERIFICATION));
        return createEmailVerifyToken(user.getUserId());
    }

    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional
    public void removeExpiredTokens() {
        LocalDateTime expiryTime = LocalDateTime.now().minusDays(1);
        authTokenRepository.deleteExpiredTokens(expiryTime);
    }
}
