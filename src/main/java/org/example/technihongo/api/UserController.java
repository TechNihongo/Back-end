package org.example.technihongo.api;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.core.mail.EmailService;
import org.example.technihongo.core.security.JWTHelper;
import org.example.technihongo.core.security.MyUserDetailsService;
import org.example.technihongo.core.security.TokenBlacklist;
import org.example.technihongo.dto.*;
import org.example.technihongo.entities.User;
import org.example.technihongo.enums.TokenType;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.AuthTokenService;
import org.example.technihongo.services.interfaces.StudentDailyLearningLogService;
import org.example.technihongo.services.interfaces.StudentService;
import org.example.technihongo.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private MyUserDetailsService myUserDetailsService;
    @Autowired
    private JWTHelper jwtHelper;
    @Autowired
    private TokenBlacklist tokenBlacklist;
    @Autowired
    private AuthTokenService authTokenService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private StudentDailyLearningLogService studentDailyLearningLogService;
    @Autowired
    private StudentService studentService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseTokenDTO> login(@RequestBody UserLogin userLogin) {
        try {
            LoginResponseDTO response = userService.login(userLogin.getEmail(), userLogin.getPassword());
            String token = myUserDetailsService.loginToken(userLogin);

            if (response.isSuccess()) {
                LocalDateTime expired = jwtHelper.getExpirationDateFromToken(token).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                authTokenService.saveLoginToken(new CreateLoginTokenDTO(response.getUserId(), token, String.valueOf(TokenType.LOGIN), expired));
                authTokenService.updateLoginTokenStatus(response.getUserId());

                Integer studentId = studentService.getStudentIdByUserId(response.getUserId());
                if(studentId != null){
                    studentDailyLearningLogService.trackStudentDailyLearningLog(studentId, 0);
                }
                return ResponseEntity.ok(new LoginResponseTokenDTO(response.getUserId(), response.getUserName(),
                        response.getEmail(), response.getRole(), true, response.getMessage(), token));

            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponseTokenDTO(response.getUserId(), response.getUserName(),
                        response.getEmail(), response.getRole(), false, response.getMessage(), token));
            }

        } catch (Exception e) {
            String errorMessage = "Login failed: " + e.getMessage();
            LoginResponseTokenDTO errorResponse = new LoginResponseTokenDTO(null, null, null, null, false, errorMessage, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDTO> register(@RequestBody RegistrationDTO registrationDTO) {
        try {
            LoginResponseDTO response = userService.register(registrationDTO);

            if (response.isSuccess()) {
                authTokenService.deactivateAllTokensByUserId(response.getUserId(), String.valueOf(TokenType.EMAIL_VERIFICATION));
                String token = authTokenService.createEmailVerifyToken(response.getUserId());
                emailService.sendVerificationEmail(response.getEmail(), token);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (RuntimeException e) {
            String errorMessage = "Registration failed: " + e.getMessage();
            LoginResponseDTO errorResponse = new LoginResponseDTO(null, null, null, null, false, errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            String errorMessage = "Registration failed: " + e.getMessage();
            LoginResponseDTO errorResponse = new LoginResponseDTO(null, null, null, null, false, errorMessage);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    @PostMapping("/google-auth")
    public ResponseEntity<LoginResponseTokenDTO> authenticateWithGoogle(@RequestBody GoogleTokenDTO googleTokenDTO) {
        try {
            LoginResponseDTO response = userService.authenticateWithGoogle(googleTokenDTO);
            UserLogin userLogin = new UserLogin(response.getEmail(), "");
            String token = myUserDetailsService.loginToken(userLogin);

            if (response.isSuccess()) {
                LocalDateTime expired = jwtHelper.getExpirationDateFromToken(token).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                authTokenService.saveLoginToken(new CreateLoginTokenDTO(response.getUserId(), token, String.valueOf(TokenType.LOGIN_GOOGLE), expired));
                authTokenService.updateLoginTokenStatus(response.getUserId());

                Integer studentId = studentService.getStudentIdByUserId(response.getUserId());
                if(studentId != null){
                    studentDailyLearningLogService.trackStudentDailyLearningLog(studentId, 0);
                }

                return ResponseEntity.ok(new LoginResponseTokenDTO(response.getUserId(), response.getUserName(),
                        response.getEmail(), response.getRole(), true, response.getMessage(), token));

            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponseTokenDTO(response.getUserId(), response.getUserName(),
                        response.getEmail(), response.getRole(), false, response.getMessage(), token));
            }
        } catch (Exception e) {
            String errorMessage = "Google authentication failed: " + e.getMessage();
            LoginResponseTokenDTO errorResponse = new LoginResponseTokenDTO(null, null, null, null, false, errorMessage, null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            authTokenService.setTokenStatus(new TokenStatusDTO(token, false));
            tokenBlacklist.addToken(token);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Logged out successfully.")
                    .build());
        } else {
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(false)
                    .message("Invalid Authorization header.")
                    .build());
        }
    }

    @GetMapping("/getUser/{userId}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Integer userId) {
        try {
            User user = userService.getUserById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.builder()
                                .success(false)
                                .message("User not found with id: " + userId)
                                .build());
            }
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("User retrieved successfully")
                    .data(user)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to retrieve user: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllUser() throws Exception {
        try{
            List<User> userList = userService.userList();
            if(userList.isEmpty()){
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(false)
                        .message("List user is empty!")
                        .build());
            }else{
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Get All User")
                        .data(userList)
                        .build());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }
    @GetMapping("/get-student")
    public ResponseEntity<ApiResponse> getStudentUsers() {
        try {
            List<User> students = userService.getStudentUsers();
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Student users retrieved successfully")
                    .data(students)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to retrieve student users: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/get-content-manager")
    public ResponseEntity<ApiResponse> getContentManagerUsers() {
        try {
            List<User> contentManagers = userService.getContentManagerUsers();
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Content manager users retrieved successfully")
                    .data(contentManagers)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to retrieve content manager users: " + e.getMessage())
                            .build());
        }
    }
    @GetMapping("/paginated")
    public ResponseEntity<ApiResponse> getAllUsersPaginated(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "userId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "") Integer roleId) {

        try {
            PageResponseDTO<User> pageResponse = userService.userListPaginated(roleId, pageNo, pageSize, sortBy, sortDir);

            if(pageResponse.getContent().isEmpty()){
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(false)
                        .message("List user is empty!")
                        .build());
            } else {
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Users retrieved successfully")
                        .data(pageResponse)
                        .build());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/student/paginated")
    public ResponseEntity<ApiResponse> getStudentUsersPaginated(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "userId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        try {
            PageResponseDTO<User> pageResponse = userService.getStudentUsersPaginated(pageNo, pageSize, sortBy, sortDir);

            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Student users retrieved successfully")
                    .data(pageResponse)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to retrieve student users: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/content-managers/paginated")
    public ResponseEntity<ApiResponse> getContentManagerUsersPaginated(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "userId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        try {
            PageResponseDTO<User> pageResponse = userService.getContentManagerUsersPaginated(pageNo, pageSize, sortBy, sortDir);

            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Content manager users retrieved successfully")
                    .data(pageResponse)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to retrieve content manager users: " + e.getMessage())
                            .build());
        }
    }



    @PatchMapping("/{userId}/username")
    public ResponseEntity<ApiResponse> updateUserName(
            @PathVariable Integer userId,
            @RequestBody UpdateProfileDTO request) {
        try {
            userService.updateUserName(userId, request);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Username updated successfully")
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("User not found: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/searchStudentName")
    public ResponseEntity<ApiResponse> searchStudent(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "createAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            PageResponseDTO<User> response = userService.searchStudent(keyword, pageNo, pageSize, sortBy, sortDir);
            if (response.getContent().isEmpty()) {
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(false)
                        .message("No User found matching the keyword: " + keyword)
                        .build());
            }
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("User matching the keyword")
                    .data(response)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to search Student: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/searchContentManagerName")
    public ResponseEntity<ApiResponse> searchContentManager(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "createAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            PageResponseDTO<User> response = userService.searchContentManager(keyword, pageNo, pageSize, sortBy, sortDir);
            if (response.getContent().isEmpty()) {
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(false)
                        .message("No ContentManager found matching the keyword: " + keyword)
                        .build());
            }
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("ContentManager matching the keyword")
                    .data(response)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to search ContentManager: " + e.getMessage())
                            .build());
        }
    }

    @PatchMapping("/{userId}/password")
    public ResponseEntity<ApiResponse> updatePassword(
            @PathVariable Integer userId,
            @RequestBody UpdateProfileDTO request) {
        try {
            userService.updatePassword(userId, request);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Password updated successfully")
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("User not found: " + e.getMessage())
                            .build());
        }
    }

    @PostMapping("/content-manager/{adminId}")
    public ResponseEntity<ApiResponse> createContentManager(
            @PathVariable("adminId") Integer adminId,
            @RequestBody ContentManagerDTO contentManagerDTO) {
        try {
            User newContentManager = userService.createContentManager(contentManagerDTO, adminId);

            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Content Manager created successfully!")
                    .data(newContentManager)
                    .build());

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.builder()
                    .success(false)
                    .message("Failed to create Content Manager: " + e.getMessage())
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.builder()
                    .success(false)
                    .message("Internal Server Error: " + e.getMessage())
                    .build());
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPass(@RequestBody EmailDTO emailDTO){
        String response = userService.forgotPass(emailDTO.getEmail());

        if(!response.startsWith("Invalid")){
            response = "http://localhost:3000/api/user/reset-password?token=" + response;
            emailService.sendSimpleEmail(emailDTO.getEmail(), "Reset Password", "Link: " + response);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Create token success! Please check email.")
                    .build());
        }
        else{
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(false)
                    .message(response)
                    .build());
        }
    }

    @PatchMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPass(@RequestParam String token, @RequestBody PasswordResetDTO passwordResetDTO) {
        try{
            String message = userService.resetPass(token, passwordResetDTO);
            boolean success = message.equals("Your password has been successfully updated.");
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(success)
                    .message(message)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @PostMapping("/resend-verification-email")
    public ResponseEntity<ApiResponse> resendVerificationEmail(@RequestParam String email) {
        try {
            String token = authTokenService.resendVerificationEmail(email);
            emailService.sendVerificationEmail(email, token);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Verification email resent successfully.")
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.builder()
                .success(false)
                .message("Failed to resend verification email: " + e.getMessage())
                .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @PatchMapping("/verify-email")
    public ResponseEntity<ApiResponse> verifyEmail(@RequestParam String token) {
        try {
            userService.verifyEmailToken(token);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Email verified successfully.")
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.builder()
                    .success(false)
                    .message("Failed to resend verification email: " + e.getMessage())
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

}
