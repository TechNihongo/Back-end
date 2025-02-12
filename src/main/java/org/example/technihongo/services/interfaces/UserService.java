package org.example.technihongo.services.interfaces;


import org.example.technihongo.dto.GoogleTokenDTO;
import org.example.technihongo.dto.GoogleUserInfoDTO;
import org.example.technihongo.dto.LoginResponseDTO;
import org.example.technihongo.dto.RegistrationDTO;
import org.example.technihongo.entities.User;

import java.util.List;

public interface UserService {
    LoginResponseDTO login(String email, String password) throws Exception;

    List<User> userList();

    LoginResponseDTO register(RegistrationDTO registrationDTO);

    LoginResponseDTO authenticateWithGoogle(GoogleTokenDTO tokenDTO);
    GoogleUserInfoDTO verifyGoogleToken(String accessToken);



}
