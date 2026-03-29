package com.stylematch.controller;

import com.stylematch.domain.User;
import com.stylematch.dto.*;
import com.stylematch.service.AuthService;
import com.stylematch.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for login, registration, and secure credentials management")
public class AuthenticationController {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthService authService;
    private final UserService userService;

    public AuthenticationController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @Operation(summary = "Register a new user")
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            log.info("Registration successful for: {}", request.getEmail());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.warn("Registration rejected — email already exists: {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Email is already registered."));
        }
    }

    @Operation(summary = "Authenticate user — uses BCryptPasswordEncoder.matches() via AuthenticationManager")
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) {
        log.info("Login attempt: {}", request.getEmail());
        try {
            AuthResponse response = authService.login(request);
            log.info("Login SUCCESS: {}", request.getEmail());
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException e) {
            log.warn("Login FAILED — user not found: {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Invalid email or password."));
        } catch (BadCredentialsException e) {
            log.warn("Login FAILED — bad credentials for: {} (BCrypt match returned false)", request.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Invalid email or password."));
        } catch (DisabledException e) {
            log.warn("Login FAILED — account disabled: {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Account is disabled."));
        } catch (Exception e) {
            log.error("Login FAILED — unexpected error for: {} — {}", request.getEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Invalid email or password."));
        }
    }

    @Operation(summary = "Request a password reset token")
    @PostMapping("/forgot-password")
    public ResponseEntity<PasswordResetResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        log.info("Password reset requested for email: {}", request.getEmail());
        userService.createPasswordResetToken(request.getEmail());

        return ResponseEntity.ok(PasswordResetResponse.builder()
            .message("If this email is registered, a reset link has been generated and sent.")
            .build());
    }

    @Operation(summary = "Reset password using a valid token")
    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        try {
            userService.resetPassword(request.getToken(), request.getNewPassword());
            return ResponseEntity.ok(new MessageResponse("Password reset successfully. You can now log in."));
        } catch (IllegalArgumentException e) {
            log.warn("Password reset REJECTED: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Change password for authenticated user")
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ChangePasswordRequest request) {
        log.info("Password change attempt for user: {}", user.getEmail());
        try {
            userService.changePassword(user, request.getCurrentPassword(), request.getNewPassword());
            return ResponseEntity.ok("Password changed successfully.");
        } catch (IllegalArgumentException e) {
            log.warn("Password change FAILED: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
