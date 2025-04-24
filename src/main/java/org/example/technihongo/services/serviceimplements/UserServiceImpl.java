package org.example.technihongo.services.serviceimplements;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.*;
import org.example.technihongo.entities.AuthToken;
import org.example.technihongo.entities.Role;
import org.example.technihongo.entities.Student;
import org.example.technihongo.entities.User;
import org.example.technihongo.enums.TokenType;
import org.example.technihongo.exception.ResourceNotFoundException;
import org.example.technihongo.repositories.AuthTokenRepository;
import org.example.technihongo.repositories.RoleRepository;
import org.example.technihongo.repositories.StudentRepository;
import org.example.technihongo.repositories.UserRepository;
import org.example.technihongo.services.interfaces.AchievementService;
import org.example.technihongo.services.interfaces.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private static final long EXPIRE_TOKEN = 10;
    private final AchievementService achievementService;

    private final RoleRepository roleRepository;
    private final AuthTokenRepository authTokenRepository;
    private final StudentRepository studentRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    private static final String GOOGLE_TOKEN_INFO_URL = "https://www.googleapis.com/oauth2/v3/tokeninfo?access_token=";

    @Override
    public LoginResponseDTO login(String email, String password) throws Exception {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("User không tồn tại"));

        if (!user.isVerified()){
            throw new Exception("Account chưa được xác thực");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new Exception("Password không đúng");
        }

        if (user.getRole().getRoleId() == 3 && user.getStudent() != null) {
            achievementService.assignAchievementsToStudent(user.getStudent().getStudentId(), LocalDateTime.now());
        }

        return new LoginResponseDTO(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.getRole().getRoleName(),
                true,
                "Login thành công!!"
        );
    }

    @Override
    @Transactional
    public LoginResponseDTO register(RegistrationDTO registrationDTO) {
        try {
            if (!registrationDTO.getPassword().equals(registrationDTO.getConfirmPassword())) {
                throw new IllegalArgumentException("Password and Confirm Password không khớp nhau");
            }
            if (userRepository.existsByUserName(registrationDTO.getUserName())) {
                throw new RuntimeException("Username đã tồn tại", new IllegalArgumentException("Username: " + registrationDTO.getUserName()));
            }

            if (userRepository.existsByEmail(registrationDTO.getEmail())) {
                User existingUser = userRepository.findUserByEmail(registrationDTO.getEmail());
                if (existingUser.isVerified()) {
                    throw new RuntimeException("Email đã tồn tại", new IllegalArgumentException("Email: " + registrationDTO.getEmail()));
                }

                existingUser.setUserName(registrationDTO.getUserName());
                existingUser.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
                existingUser.setDob(registrationDTO.getDob());
                userRepository.save(existingUser);

                Student student = studentRepository.findByUser_UserId(existingUser.getUserId());
                student.setOccupation(registrationDTO.getOccupation());
                studentRepository.save(student);

                return new LoginResponseDTO(
                        existingUser.getUserId(),
                        existingUser.getUserName(),
                        existingUser.getEmail(),
                        existingUser.getRole().getRoleName(),
                        true,
                        "Registration updated. Please check your email for verification."
                );
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
                    .profileImg("https://cdn-icons-png.flaticon.com/512/8801/8801434.png")
                    .isActive(true)
                    .role(defaultRole)
                    .build();
            Student student = Student.builder()
                    .user(user)
                    .dailyGoal(60)
                    .occupation(registrationDTO.getOccupation())
                    .reminderEnabled(false)
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
                        "Registration thành công! Vui lòng kiểm tra email để xác thực tài khoản!"
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
            message = "Login thành công";
        } else {
            user = createNewUserFromGoogle(googleUserInfoDTO);
            message = "Registration thành công. Vui lòng kiểm tra email để xác thực tài khoản!";
        }

        user = userRepository.save(user);

        if (user.getRole().getRoleId() == 3 && user.getStudent() != null) {
            achievementService.assignAchievementsToStudent(user.getStudent().getStudentId(), LocalDateTime.now());
        }

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
                    .password(passwordEncoder.encode("GOOGLE_AUTH_USER"))
                    .isActive(true)
                    .role(defaultRole)
                    .profileImg(googleUser.getPicture())
                    .uid(googleUser.getSub())
                    .createdAt(LocalDateTime.now())
                    .lastLogin(LocalDateTime.now())
                    .isVerified(true)
                    .build();

            Student student = Student.builder()
                    .user(user)
                    .dailyGoal(60)
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
            throw new RuntimeException("Chỉ Admin mới có quyền tạo Content Manager");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }

        if (userRepository.existsByUserName(dto.getUserName())) {
            throw new RuntimeException("Username đã tồn tại");
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
                .isVerified(true)
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
        if(user == null) throw new RuntimeException("Không tìm thấy User!");
        user.setUserName(dto.getUserName());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updatePassword(Integer userId, UpdateProfileDTO dto) {
        if (userId == null || dto == null || dto.getPassword() == null || dto.getConfirmPassword() == null) {
            throw new IllegalArgumentException("User ID, Password, or Confirm Password không được để trống");
        }
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("Password and Confirm Password không khớp nhau");
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

    @Override
    public List<User> getStudentUsers() {
        return userRepository.findByRole_RoleId(3);
    }

    @Override
    public List<User> getContentManagerUsers() {
        return userRepository.findByRole_RoleId(2);
    }

    @Override
    public PageResponseDTO<User> userListPaginated(Integer roleId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<User> users;
        if(roleId != null) {
            users = userRepository.findByRole_RoleId(roleId, pageable);
        }
        else {
            users = userRepository.findAll(pageable);
        }

        return getPageResponseDTO(users);
    }

    @Override
    public PageResponseDTO<User> getStudentUsersPaginated(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<User> users = userRepository.findByRole_RoleId(3, pageable);

        return getPageResponseDTO(users);
    }

    @Override
    public PageResponseDTO<User> getContentManagerUsersPaginated(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<User> users = userRepository.findByRole_RoleId(2, pageable);

        return getPageResponseDTO(users);
    }

    @Override
    public User getUserById(Integer userId) {
        return userRepository.findByUserId(userId);
    }


    @Override
    public PageResponseDTO<User> searchStudent(String keyword, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<User> users;

        if (keyword == null || keyword.trim().isEmpty()) {
            users = userRepository.findByRole_RoleId(3, pageable);
        } else {
            users = userRepository.findStudentsByKeyword(keyword.trim(), pageable);
        }

        return getPageResponseDTO(users);
    }

    @Override
    public PageResponseDTO<User> searchContentManager(String keyword, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<User> users;

        if (keyword == null || keyword.trim().isEmpty()) {
            users = userRepository.findByRole_RoleId(2, pageable);
        } else {
            users = userRepository.findContentManagersByKeyword(keyword.trim(), pageable);
        }

        return getPageResponseDTO(users);
    }

    @Override
    public String forgotPass(String email) {
        User user = userRepository.findUserByEmail(email);

        if (user == null){
            return "Invalid email.";
        }

        AuthToken authToken = AuthToken.builder()
                .user(user)
                .token(generateToken())
                .tokenType(TokenType.RESET_PASSWORD)
                .expiresAt(LocalDateTime.now().plusMinutes(EXPIRE_TOKEN))
                .build();

        authTokenRepository.save(authToken);
        return authToken.getToken();
    }

    @Override
    public String resetPass(String token, PasswordResetDTO passwordResetDTO) {
        AuthToken authToken = authTokenRepository.findByToken(token);
        User user = userRepository.findByUserId(authToken.getUser().getUserId());

        if (user == null) {
            return "Invalid token";
        }

        if (authToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            authToken.setIsActive(false);
            authTokenRepository.save(authToken);
            return "Token expired.";
        }

        if (!passwordResetDTO.getPassword().equals(passwordResetDTO.getConfirm())) {
            return "Confirm password không khớp!";
        }


        if (passwordEncoder.matches(passwordResetDTO.getPassword(), user.getPassword())) {
            return "New password không được giống với Old Password.";
        }

        user.setPassword(passwordEncoder.encode(passwordResetDTO.getPassword()));

        userRepository.save(user);
        authToken.setIsActive(false);
        authTokenRepository.save(authToken);
        return "Your password đã được cập nhật thành công!";
    }

    private String generateToken() {
        String token = String.valueOf(UUID.randomUUID()) +
                UUID.randomUUID();
        return token;
    }

    @Override
    public void verifyEmailToken(String token) {
        AuthToken authToken = authTokenRepository.findByTokenAndTokenTypeAndIsActive(token, TokenType.EMAIL_VERIFICATION, true)
                .orElseThrow(() -> new RuntimeException("Invalid or expired token."));

        if (authToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token has expired.");
        }

        User user = authToken.getUser();
        user.setVerified(true);
        userRepository.save(user);

        authToken.setIsActive(false);
        authTokenRepository.save(authToken);
    }

    @Override
    public UserDTO getUserByStudentId(Integer studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
        User user = student.getUser();
        return new UserDTO(user.getUserId(), user.getUserName(), user.getEmail(), user.getDob(), user.getProfileImg());
    }


    private PageResponseDTO<User> getPageResponseDTO(Page<User> page) {
        return PageResponseDTO.<User>builder()
                .content(page.getContent())
                .pageNo(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

}