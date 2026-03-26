package com.stylematch.controller;

import com.stylematch.domain.User;
import com.stylematch.dto.ChangePasswordRequest;
import com.stylematch.dto.LanguageRequest;
import com.stylematch.dto.UpdateUserProfileRequest;
import com.stylematch.dto.UserProfileResponse;
import com.stylematch.dto.ProfileResultsResponse;
import com.stylematch.service.PasswordService;
import com.stylematch.service.TestResultService;
import com.stylematch.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "Endpoints for managing user profile settings")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final PasswordService passwordService;
    private final UserService userService;
    private final TestResultService testResultService;

    public UserController(PasswordService passwordService, UserService userService, TestResultService testResultService) {
        this.passwordService = passwordService;
        this.userService = userService;
        this.testResultService = testResultService;
    }

    @Operation(summary = "Change password", description = "Allows authenticated users to change their password securely.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Incorrect old password or invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ChangePasswordRequest request) {
        try {
            passwordService.changePassword(user, request);
            return ResponseEntity.ok("Password changed successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Change preferred language", description = "Updates the authenticated user's preferred language.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Language updated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PutMapping("/language")
    public ResponseEntity<String> changeLanguage(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody LanguageRequest request) {
        userService.changeLanguage(user, request.getLanguage());
        return ResponseEntity.ok("Language updated successfully");
    }

    @Operation(summary = "Get user profile", description = "Returns the authenticated user's profile.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile returned successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getProfile(Authentication authentication) {
        log.info("GET /api/users/me called by {}", authentication.getName());
        return ResponseEntity.ok(userService.getProfileByEmail(authentication.getName()));
    }

    @Operation(summary = "Get user test results", description = "Returns grouped test results for the authenticated user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Results returned successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping({"/me/results", "/results"})
    public ResponseEntity<ProfileResultsResponse> getResults(Authentication authentication) {
        String email = authentication.getName();
        log.info("GET /api/users/results called by {}", email);
        // Find user by email first
        User user = (User) userService.loadUserByUsername(email);
        return ResponseEntity.ok(testResultService.getGroupedResults(user));
    }

    @Operation(summary = "Update user profile", description = "Updates profile fields (name, age, weight, height) for the authenticated user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PutMapping("/me")
    public ResponseEntity<UserProfileResponse> updateProfile(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody UpdateUserProfileRequest request) {
        return ResponseEntity.ok(userService.updateUserProfile(user, request));
    }

    @Operation(summary = "Delete user account")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Account deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteAccount(@AuthenticationPrincipal User user) {
        userService.deleteUserAccount(user.getId());
        return ResponseEntity.noContent().build();
    }
}
