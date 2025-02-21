package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.CreateLoginTokenDTO;
import org.example.technihongo.dto.TokenStatusDTO;

public interface AuthTokenService {
    void saveLoginToken(CreateLoginTokenDTO createLoginTokenDTO);
    void setTokenStatus(TokenStatusDTO tokenStatus);
    void updateLoginTokenStatus(Integer userId);
}
