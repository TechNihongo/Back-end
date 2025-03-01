package org.example.technihongo.api;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.core.mail.EmailService;
import org.example.technihongo.core.security.JWTHelper;
import org.example.technihongo.core.security.MyUserDetailsService;
import org.example.technihongo.core.security.TokenBlacklist;
import org.example.technihongo.dto.*;
import org.example.technihongo.entities.User;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.AuthTokenService;
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

    @PostMapping("/login")
    public ResponseEntity<LoginResponseTokenDTO> login(@RequestBody UserLogin userLogin) {
        try {
            LoginResponseDTO response = userService.login(userLogin.getEmail(), userLogin.getPassword());
            String token = myUserDetailsService.loginToken(userLogin);
            LocalDateTime expired = jwtHelper.getExpirationDateFromToken(token).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            authTokenService.saveLoginToken(new CreateLoginTokenDTO(response.getUserId(), token, "LOGIN", expired));
            authTokenService.updateLoginTokenStatus(response.getUserId());

            if (response.isSuccess()) {
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
    public ResponseEntity<LoginResponseDTO> authenticateWithGoogle(@RequestBody GoogleTokenDTO googleTokenDTO) {
        try {
            LoginResponseDTO response = userService.authenticateWithGoogle(googleTokenDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            String errorMessage = "Google authentication failed: " + e.getMessage();
            LoginResponseDTO errorResponse = new LoginResponseDTO(null, null, null, null, false, errorMessage);
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

    @PutMapping("/reset-password")
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
}
