package org.example.technihongo.services.serviceimplements;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.CreateLoginTokenDTO;
import org.example.technihongo.dto.TokenStatusDTO;
import org.example.technihongo.entities.AuthToken;
import org.example.technihongo.repositories.AuthTokenRepository;
import org.example.technihongo.repositories.UserRepository;
import org.example.technihongo.services.interfaces.AuthTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
                .tokenType(createLoginTokenDTO.getTokenType())
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
                        authToken.getTokenType().equalsIgnoreCase("LOGIN") &&
                        authToken.getIsActive())
                .toList();

        for(AuthToken a : authTokenList){
            if(a.getExpiresAt().isBefore(LocalDateTime.now())){
                a.setIsActive(false);
            }
        }

        authTokenRepository.saveAll(authTokenList);
    }
}
