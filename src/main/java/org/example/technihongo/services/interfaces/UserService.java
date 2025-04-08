package org.example.technihongo.services.interfaces;


import org.example.technihongo.dto.*;
import org.example.technihongo.entities.User;

import java.util.List;

public interface UserService {
    LoginResponseDTO login(String email, String password) throws Exception;

    List<User> userList();
    List<User> getStudentUsers();
    List<User> getContentManagerUsers();


    PageResponseDTO<User> userListPaginated(Integer roleId, int pageNo, int pageSize, String sortBy, String sortDir);
    PageResponseDTO<User> getStudentUsersPaginated(int pageNo, int pageSize, String sortBy, String sortDir);
    PageResponseDTO<User> getContentManagerUsersPaginated(int pageNo, int pageSize, String sortBy, String sortDir);
    LoginResponseDTO register(RegistrationDTO registrationDTO);


    User getUserById(Integer userId);
    LoginResponseDTO authenticateWithGoogle(GoogleTokenDTO tokenDTO);
    GoogleUserInfoDTO verifyGoogleToken(String accessToken);

    User createContentManager(ContentManagerDTO dto, Integer userId);

    void updateUserName(Integer userId, UpdateProfileDTO dto);
    void updatePassword(Integer userId, UpdateProfileDTO dto);

    String forgotPass(String email);
    String resetPass(String token, PasswordResetDTO passwordResetDTO);

    PageResponseDTO<User> searchStudent(String keyword, int pageNo, int pageSize, String sortBy, String sortDir);
    PageResponseDTO<User> searchContentManager(String keyword, int pageNo, int pageSize, String sortBy, String sortDir);

    void verifyEmailToken(String token);

    Integer getUserByStudentId(Integer studentId);
}
