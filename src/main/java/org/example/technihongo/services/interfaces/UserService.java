package org.example.technihongo.services.interfaces;


import org.example.technihongo.dto.*;
import org.example.technihongo.entities.User;

import java.util.List;

public interface UserService {
    LoginResponseDTO login(String email, String password) throws Exception;

    List<User> userList();

    LoginResponseDTO register(RegistrationDTO registrationDTO);

    LoginResponseDTO authenticateWithGoogle(GoogleTokenDTO tokenDTO);
    GoogleUserInfoDTO verifyGoogleToken(String accessToken);

    User createContentManager(ContentManagerDTO dto, Integer userId);

    void updateUserName(Integer userId, UpdateProfileDTO dto);
    void updatePassword(Integer userId, UpdateProfileDTO dto);

    String forgotPass(String email);
    String resetPass(String token, PasswordResetDTO passwordResetDTO);
}
