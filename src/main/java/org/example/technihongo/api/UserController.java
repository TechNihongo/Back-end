package org.example.technihongo.api;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.*;
import org.example.technihongo.entities.User;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody UserLogin userLogin) {
        try {
            LoginResponseDTO response = userService.login(userLogin.getEmail(), userLogin.getPassword());

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            String errorMessage = "Login failed: " + e.getMessage();
            LoginResponseDTO errorResponse = new LoginResponseDTO(null, null, null, null, false, errorMessage);
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


    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllUser() throws Exception {
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
}
