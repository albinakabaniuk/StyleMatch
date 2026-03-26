package com.stylematch.service;

import com.stylematch.domain.User;
import com.stylematch.dto.ChangePasswordRequest;
import com.stylematch.repository.UserRepository;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswordServiceTest {

    @Mock
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    private PasswordService passwordService;

    private User testUser;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        passwordService = new PasswordService(userRepository, passwordEncoder);

        testUser = User.builder()
                .id(UUID.randomUUID())
                .email("test@example.com")
                .password(passwordEncoder.encode("oldPassword123")) // Encode the password here for consistency with tests
                .build();
    }

    @Test
    void testPasswordEncodingAndVerification() {
        String rawPassword = "mySecretPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertNotEquals(rawPassword, encodedPassword);
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
        assertFalse(passwordEncoder.matches("wrongPassword", encodedPassword));
    }

    @Test
    void changePassword_Success() {
        // Given
        ChangePasswordRequest request = new ChangePasswordRequest("oldPassword123", "newPassword456");

        // When
        passwordService.changePassword(testUser, request);

        // Then
        assertTrue(passwordEncoder.matches("newPassword456", testUser.getPassword()));
        verify(userRepository).save(testUser);
    }

    @Test
    void changePassword_IncorrectOldPassword() {
        // Given
        ChangePasswordRequest request = new ChangePasswordRequest("wrongOldPassword", "newPassword456");

        // When/Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            passwordService.changePassword(testUser, request);
        });

        assertEquals("Incorrect current password", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
}
