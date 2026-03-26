package com.stylematch.service;

import com.stylematch.domain.User;
import com.stylematch.dto.UpdateUserProfileRequest;
import com.stylematch.dto.UserProfileResponse;
import com.stylematch.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(UUID.randomUUID())
                .email("test@example.com")
                .name("Old Name")
                .preferredLanguage("en")
                .password("encodedPassword")
                .build();
    }

    @Test
    void updateUserProfile_Success() {
        // Given
        UpdateUserProfileRequest request = UpdateUserProfileRequest.builder()
                .name("New Name")
                .age(30)
                .preferredLanguage("uk")
                .build();

        // When
        UserProfileResponse response = userService.updateUserProfile(testUser, request);

        // Then
        assertEquals("New Name", testUser.getName());
        assertEquals(30, testUser.getAge());
        assertEquals("uk", testUser.getPreferredLanguage());
        verify(userRepository).save(testUser);
        assertNotNull(response);
    }

    @Test
    void changeLanguage_Success() {
        // Given
        String newLang = "fr";

        // When
        userService.changeLanguage(testUser, newLang);

        // Then
        assertEquals("fr", testUser.getPreferredLanguage());
        verify(userRepository).save(testUser);
    }

    @Test
    void changePassword_Success() {
        // Given
        String currentPassword = "oldPassword";
        String newPassword = "newPassword";
        when(passwordEncoder.matches(currentPassword, testUser.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn("newEncodedPassword");

        // When
        userService.changePassword(testUser, currentPassword, newPassword);

        // Then
        assertEquals("newEncodedPassword", testUser.getPassword());
        verify(userRepository).save(testUser);
    }

    @Test
    void changePassword_WrongCurrentPassword() {
        // Given
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // When/Then
        assertThrows(IllegalArgumentException.class, () -> {
            userService.changePassword(testUser, "wrong", "new");
        });
        verify(userRepository, never()).save(any());
    }
}
