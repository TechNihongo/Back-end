package org.example.technihongo.services.serviceimplements;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.*;
import org.example.technihongo.entities.Role;
import org.example.technihongo.entities.Student;
import org.example.technihongo.entities.User;
import org.example.technihongo.exception.ResourceNotFoundException;
import org.example.technihongo.repositories.RoleRepository;
import org.example.technihongo.repositories.UserRepository;
import org.example.technihongo.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    private static final String GOOGLE_TOKEN_INFO_URL = "https://www.googleapis.com/oauth2/v3/tokeninfo?access_token=";

    @Override
    public LoginResponseDTO login(String email, String password) throws Exception {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("User does not exist"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new Exception("Invalid password");
        }
        return new LoginResponseDTO(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.getRole().getRoleName(),
                true,
                "Login Successfully"
        );
    }

    @Override
    @Transactional
    public LoginResponseDTO register(RegistrationDTO registrationDTO) {
        try {
            if (!registrationDTO.getPassword().equals(registrationDTO.getConfirmPassword())) {
                throw new IllegalArgumentException("Password and Confirm Password do not match");
            }
            if (userRepository.existsByEmail(registrationDTO.getEmail())) {
                throw new RuntimeException("Email already exists", new IllegalArgumentException("Email: " + registrationDTO.getEmail()));
            }
            if (userRepository.existsByUserName(registrationDTO.getUserName())) {
                throw new RuntimeException("Username already exists", new IllegalArgumentException("Username: " + registrationDTO.getUserName()));
            }
            Role defaultRole;
            try {
                defaultRole = roleRepository.findById(3)
                        .orElseThrow(() -> new EntityNotFoundException("Role with ID 3 not found"));
            } catch (EntityNotFoundException e) {
                throw new RuntimeException("Failed to find default role for registration", e);
            }
            User user = User.builder()
                    .userName(registrationDTO.getUserName())
                    .email(registrationDTO.getEmail())
                    .password(passwordEncoder.encode(registrationDTO.getPassword()))
                    .dob(registrationDTO.getDob())
                    .isActive(true)
                    .role(defaultRole)
                    .build();
            Student student = Student.builder()
                    .user(user)
                    .occupation(registrationDTO.getOccupation())
                    .reminderEnabled(true)
                    .build();

            user.setStudent(student);

            try {
                User savedUser = userRepository.save(user);
                return new LoginResponseDTO(
                        savedUser.getUserId(),
                        savedUser.getUserName(),
                        savedUser.getEmail(),
                        savedUser.getRole().getRoleName(),
                        true,
                        "Registration Successful"
                );
            } catch (DataIntegrityViolationException e) {
                throw new RuntimeException("Failed to save user data", e);
            }
        } catch (Exception e) {
            throw new RuntimeException("Registration failed: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public LoginResponseDTO authenticateWithGoogle(GoogleTokenDTO tokenDTO) {
        GoogleUserInfoDTO googleUserInfoDTO = verifyGoogleToken(tokenDTO.getAccessToken());

        if (googleUserInfoDTO == null || !googleUserInfoDTO.isEmail_verified()) {
            throw new RuntimeException("Invalid Google token or email not verified");
        }

        Optional<User> existedUser = userRepository.findByEmail(googleUserInfoDTO.getEmail());

        User user;
        String message;

        if (existedUser.isPresent()) {
            user = existedUser.get();
            user.setLastLogin(LocalDateTime.now());
            message = "Login Successfully";
        } else {
            user = createNewUserFromGoogle(googleUserInfoDTO);
            message = "Registration Successfully";
        }

        user = userRepository.save(user);

        return new LoginResponseDTO(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.getRole().getRoleName(),
                true,
                message
        );
    }

    private User createNewUserFromGoogle(GoogleUserInfoDTO googleUser) {
        Role defaultRole;
        try {
            defaultRole = roleRepository.findById(3)
                    .orElseThrow(() -> new EntityNotFoundException("Role with ID 3 not found"));
        } catch (EntityNotFoundException e) {
            throw new RuntimeException("Failed to find default role for Google registration", e);
        }

        try {
            User user = User.builder()
                    .email(googleUser.getEmail())
                    .userName(generateUsername(googleUser.getEmail()))
                    .isActive(true)
                    .role(defaultRole)
                    .profileImg(googleUser.getPicture())
                    .uid(googleUser.getSub())
                    .createdAt(LocalDateTime.now())
                    .lastLogin(LocalDateTime.now())
                    .build();

            Student student = Student.builder()
                    .user(user)
                    .reminderEnabled(true)
                    .build();

            user.setStudent(student);
            return user;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create user from Google data", e);
        }
    }

    private String generateUsername(String email) {
        String baseUsername = email.split("@")[0];
        String username = baseUsername;
        int counter = 1;

        while (userRepository.existsByUserName(username)) {
            username = baseUsername + counter++;
        }

        return username;
    }

    @Override
    public GoogleUserInfoDTO verifyGoogleToken(String accessToken) {
        try {
            String url = GOOGLE_TOKEN_INFO_URL + accessToken;
            GoogleUserInfoDTO userInfo = restTemplate.getForObject(url, GoogleUserInfoDTO.class);

            if (userInfo == null || userInfo.getEmail() == null) {
                throw new RuntimeException("Failed to verify Google token");
            }

            return userInfo;
        } catch (Exception e) {
            throw new RuntimeException("Error verifying Google token: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public User createContentManager(ContentManagerDTO dto, Integer userId) {
        User adminUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (adminUser.getRole().getRoleId() != 1) {
            throw new RuntimeException("Only Admin can create Content Manager accounts");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (userRepository.existsByUserName(dto.getUserName())) {
            throw new RuntimeException("Username already exists");
        }

        Role contentManagerRole = roleRepository.findById(2)
                .orElseThrow(() -> new RuntimeException("Content Manager role not found"));

        User newUser = User.builder()
                .userName(dto.getUserName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .isActive(true)
                .role(contentManagerRole)
                .createdAt(LocalDateTime.now())
                .build();

        try {
            return userRepository.save(newUser);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Failed to save Content Manager account", e);
        }
    }

    @Override
    @Transactional
    public void updateUserName(Integer userId, UpdateProfileDTO dto) {
        User user = userRepository.findByUserId(userId);
        if(user == null) throw new RuntimeException("User not found!");
        user.setUserName(dto.getUserName());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updatePassword(Integer userId, UpdateProfileDTO dto) {
        if (userId == null || dto == null || dto.getPassword() == null || dto.getConfirmPassword() == null) {
            throw new IllegalArgumentException("User ID, Password, or Confirm Password cannot be null");
        }
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("Password and Confirm Password do not match");
        }
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }


    @Override
    public List<User> userList() {
        return userRepository.findAll();
    }
}