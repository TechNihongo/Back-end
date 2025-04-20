package org.example.technihongo.services.serviceimplements;

import org.example.technihongo.dto.LoginResponseDTO;
import org.example.technihongo.dto.PasswordResetDTO;
import org.example.technihongo.entities.AuthToken;
import org.example.technihongo.entities.Role;
import org.example.technihongo.entities.Student;
import org.example.technihongo.entities.User;
import org.example.technihongo.repositories.UserRepository;
import org.example.technihongo.services.interfaces.AchievementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AchievementService achievementService;
    @Mock
    private org.example.technihongo.repositories.RoleRepository roleRepository;
    @Mock
    private org.example.technihongo.repositories.AuthTokenRepository authTokenRepository;
    @Mock
    private org.example.technihongo.repositories.StudentRepository studentRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userServiceImpl = new UserServiceImpl(
                userRepository,
                restTemplate,
                achievementService,
                roleRepository,
                authTokenRepository,
                studentRepository,
                passwordEncoder
        );
    }

    @Test
    void testLoginWithValidCredentialsReturnsSuccess() throws Exception {
        String email = "test@example.com";
        String password = "Password123!";
        String encodedPassword = "$2a$10$encodedPassword";
        Role role = Role.builder().roleId(3).roleName("Student").build();
        Student student = Student.builder().studentId(1).build();
        User user = User.builder()
                .userId(1)
                .userName("testuser")
                .email(email)
                .password(encodedPassword)
                .isVerified(true)
                .role(role)
                .student(student)
                .build();
        student.setUser(user);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);

        doNothing().when(achievementService).assignAchievementsToStudent(eq(student.getStudentId()), any(LocalDateTime.class));

        LoginResponseDTO response = userServiceImpl.login(email, password);

        assertNotNull(response);
        assertEquals(user.getUserId(), response.getUserId());
        assertEquals(user.getUserName(), response.getUserName());
        assertEquals(user.getEmail(), response.getEmail());
        assertEquals(user.getRole().getRoleName(), response.getRole());
        assertTrue(response.isSuccess());
        assertEquals("Login Successful!!", response.getMessage());

        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(password, encodedPassword);
        verify(achievementService, times(1)).assignAchievementsToStudent(eq(student.getStudentId()), any(LocalDateTime.class));
    }

    @Test
    void testLoginReturnValueWithValidCredentials() throws Exception {
        // Arrange
        String email = "testuser2@example.com";
        String password = "ValidPass123!";
        String encodedPassword = "$2a$10$encodedPassword2";
        Role role = Role.builder().roleId(3).roleName("Student").build();
        Student student = Student.builder().studentId(2).build();
        User user = User.builder()
                .userId(2)
                .userName("testuser2")
                .email(email)
                .password(encodedPassword)
                .isVerified(true)
                .role(role)
                .student(student)
                .build();
        student.setUser(user);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
        doNothing().when(achievementService).assignAchievementsToStudent(eq(student.getStudentId()), any(LocalDateTime.class));

        // Act
        LoginResponseDTO result = userServiceImpl.login(email, password);

        // Assert
        assertNotNull(result);
        assertEquals(user.getUserId(), result.getUserId());
        assertEquals(user.getUserName(), result.getUserName());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getRole().getRoleName(), result.getRole());
        assertTrue(result.isSuccess());
        assertEquals("Login Successful!!", result.getMessage());

        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(password, encodedPassword);
        verify(achievementService, times(1)).assignAchievementsToStudent(eq(student.getStudentId()), any(LocalDateTime.class));
    }

    @Test
    void testResetPassword_SuccessfulReset() {
        String token = "validToken";
        String rawPassword = "NewPassword1!";
        String encodedPassword = "$2a$10$encodedOldPassword";
        String encodedNewPassword = "$2a$10$encodedNewPassword";
        LocalDateTime now = LocalDateTime.now().plusMinutes(10);

        User user = User.builder()
                .userId(1)
                .password(encodedPassword)
                .build();

        AuthToken authToken = AuthToken.builder()
                .token(token)
                .user(user)
                .expiresAt(now)
                .isActive(true)
                .build();

        PasswordResetDTO dto = new PasswordResetDTO(rawPassword, rawPassword);

        when(authTokenRepository.findByToken(token)).thenReturn(authToken);
        when(userRepository.findByUserId(user.getUserId())).thenReturn(user);
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedNewPassword);

        String result = userServiceImpl.resetPass(token, dto);

        assertEquals("Your password has been successfully updated.", result);
        verify(userRepository).save(argThat(u -> u.getPassword().equals(encodedNewPassword)));
        verify(authTokenRepository).save(argThat(a -> !a.getIsActive()));
    }

    @Test
    void testResetPassword_DeactivatesTokenOnSuccess() {
        String token = "validToken";
        String rawPassword = "NewPassword1!";
        String encodedPassword = "$2a$10$encodedOldPassword";
        String encodedNewPassword = "$2a$10$encodedNewPassword";
        LocalDateTime now = LocalDateTime.now().plusMinutes(10);

        User user = User.builder()
                .userId(2)
                .password(encodedPassword)
                .build();

        AuthToken authToken = AuthToken.builder()
                .token(token)
                .user(user)
                .expiresAt(now)
                .isActive(true)
                .build();

        PasswordResetDTO dto = new PasswordResetDTO(rawPassword, rawPassword);

        when(authTokenRepository.findByToken(token)).thenReturn(authToken);
        when(userRepository.findByUserId(user.getUserId())).thenReturn(user);
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedNewPassword);

        userServiceImpl.resetPass(token, dto);

        verify(authTokenRepository).save(argThat(a -> !a.getIsActive()));
    }

    @Test
    void testResetPassword_UpdatesUserPassword() {
        String token = "validToken";
        String rawPassword = "AnotherNewPass1!";
        String encodedPassword = "$2a$10$encodedOldPassword";
        String encodedNewPassword = "$2a$10$encodedNewPassword";
        LocalDateTime now = LocalDateTime.now().plusMinutes(10);

        User user = User.builder()
                .userId(3)
                .password(encodedPassword)
                .build();

        AuthToken authToken = AuthToken.builder()
                .token(token)
                .user(user)
                .expiresAt(now)
                .isActive(true)
                .build();

        PasswordResetDTO dto = new PasswordResetDTO(rawPassword, rawPassword);

        when(authTokenRepository.findByToken(token)).thenReturn(authToken);
        when(userRepository.findByUserId(user.getUserId())).thenReturn(user);
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedNewPassword);

        userServiceImpl.resetPass(token, dto);

        verify(userRepository).save(argThat(u -> u.getPassword().equals(encodedNewPassword)));
    }

    @Test
    void testResetPassword_InvalidToken() {
        String token = "invalidToken";
        AuthToken authToken = mock(AuthToken.class);
        when(authTokenRepository.findByToken(token)).thenReturn(authToken);
        when(authToken.getUser()).thenReturn(null);

        PasswordResetDTO dto = new PasswordResetDTO("irrelevant", "irrelevant");

        when(userRepository.findByUserId(any())).thenReturn(null);

        String result = userServiceImpl.resetPass(token, dto);

        assertEquals("Invalid token", result);
        verify(userRepository, never()).save(any());
        verify(authTokenRepository, never()).save(any());
    }

    @Test
    void testResetPassword_ExpiredToken() {
        String token = "expiredToken";
        String rawPassword = "ExpiredPass1!";
        String encodedPassword = "$2a$10$encodedOldPassword";
        LocalDateTime expiredTime = LocalDateTime.now().minusMinutes(5);

        User user = User.builder()
                .userId(4)
                .password(encodedPassword)
                .build();

        AuthToken authToken = AuthToken.builder()
                .token(token)
                .user(user)
                .expiresAt(expiredTime)
                .isActive(true)
                .build();

        PasswordResetDTO dto = new PasswordResetDTO(rawPassword, rawPassword);

        when(authTokenRepository.findByToken(token)).thenReturn(authToken);
        when(userRepository.findByUserId(user.getUserId())).thenReturn(user);

        String result = userServiceImpl.resetPass(token, dto);

        assertEquals("Token expired.", result);
        verify(authTokenRepository).save(argThat(a -> !a.getIsActive()));
        verify(userRepository, never()).save(any());
    }

    @Test
    void testResetPassword_PasswordConfirmationMismatch() {
        String token = "validToken";
        String rawPassword = "MismatchPass1!";
        String confirmPassword = "MismatchPass2!";
        String encodedPassword = "$2a$10$encodedOldPassword";
        LocalDateTime now = LocalDateTime.now().plusMinutes(10);

        User user = User.builder()
                .userId(5)
                .password(encodedPassword)
                .build();

        AuthToken authToken = AuthToken.builder()
                .token(token)
                .user(user)
                .expiresAt(now)
                .isActive(true)
                .build();

        PasswordResetDTO dto = new PasswordResetDTO(rawPassword, confirmPassword);

        when(authTokenRepository.findByToken(token)).thenReturn(authToken);
        when(userRepository.findByUserId(user.getUserId())).thenReturn(user);

        String result = userServiceImpl.resetPass(token, dto);

        assertEquals("Confirm password does not match!", result);
        verify(userRepository, never()).save(any());
        verify(authTokenRepository, never()).save(any());
    }

    @Test
    void testGetStudentUsers_ReturnsEmptyListWhenNoStudents() {
        when(userRepository.findByRole_RoleId(3)).thenReturn(Collections.emptyList());
        List<User> result = userServiceImpl.getStudentUsers();
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findByRole_RoleId(3);
    }

    @Test
    void testGetStudentUsers_ExcludesNonStudentRoles() {
        Role studentRole = Role.builder().roleId(3).roleName("Student").build();
        Role managerRole = Role.builder().roleId(2).roleName("Manager").build();
        User studentUser = User.builder().userId(1).userName("student1").role(studentRole).build();
        User managerUser = User.builder().userId(2).userName("manager1").role(managerRole).build();

        // Only studentUser should be returned by the repository
        when(userRepository.findByRole_RoleId(3)).thenReturn(Collections.singletonList(studentUser));
        List<User> result = userServiceImpl.getStudentUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(3, result.get(0).getRole().getRoleId());
        assertNotEquals(managerUser.getUserId(), result.get(0).getUserId());
        verify(userRepository, times(1)).findByRole_RoleId(3);
    }

    @Test
    void testGetStudentUsers_ReturnsMultipleStudents() {
        Role studentRole = Role.builder().roleId(3).roleName("Student").build();
        User student1 = User.builder().userId(1).userName("student1").role(studentRole).build();
        User student2 = User.builder().userId(2).userName("student2").role(studentRole).build();
        List<User> students = Arrays.asList(student1, student2);

        when(userRepository.findByRole_RoleId(3)).thenReturn(students);
        List<User> result = userServiceImpl.getStudentUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(u -> u.getRole().getRoleId() == 3));
        verify(userRepository, times(1)).findByRole_RoleId(3);
    }

    @Test
    void testGetStudentUsers_HandlesNullRepositoryResponse() {
        when(userRepository.findByRole_RoleId(3)).thenReturn(null);
        List<User> result = null;
        try {
            result = userServiceImpl.getStudentUsers();
        } catch (NullPointerException e) {
            // The method should not throw NullPointerException, so fail if it does
            fail("Method should handle null repository response gracefully");
        }
        // Depending on implementation, result may be null or empty list; here we check for null
        assertNull(result, "Expected null if repository returns null");
        verify(userRepository, times(1)).findByRole_RoleId(3);
    }

    @Test
    void testGetStudentUsers_HandlesRepositoryException() {
        when(userRepository.findByRole_RoleId(3)).thenThrow(new RuntimeException("DB error"));
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> userServiceImpl.getStudentUsers());
        assertEquals("DB error", thrown.getMessage());
        verify(userRepository, times(1)).findByRole_RoleId(3);
    }

    @Test
    void testGetStudentUsers_InvalidOrMissingRoleId() {
        // Simulate that roleId 3 does not exist, so repository returns empty list
        when(userRepository.findByRole_RoleId(3)).thenReturn(Collections.emptyList());
        List<User> result = userServiceImpl.getStudentUsers();
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findByRole_RoleId(3);
    }
}
