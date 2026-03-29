package com.stylematch.controller;

import com.stylematch.domain.PasswordResetToken;
import com.stylematch.domain.User;
import com.stylematch.dto.AuthRequest;
import com.stylematch.dto.AuthResponse;
import com.stylematch.dto.ForgotPasswordRequest;
import com.stylematch.dto.MessageResponse;
import com.stylematch.dto.RegisterRequest;
import com.stylematch.dto.ResetPasswordRequest;
import com.stylematch.dto.ChangePasswordRequest;
import com.stylematch.repository.PasswordResetTokenRepository;
import com.stylematch.repository.UserRepository;
import com.stylematch.service.AuthService;
import com.stylematch.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for login, registration, and secure credentials management")
public class AuthenticationController {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthService authService;
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final com.stylematch.service.EmailService emailService;

    public AuthenticationController(AuthService authService, UserRepository userRepository,
                                  PasswordResetTokenRepository passwordResetTokenRepository,
                                  PasswordEncoder passwordEncoder, UserService userService,
                                  com.stylematch.service.EmailService emailService) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.emailService = emailService;
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

    @Operation(summary = "Request a password reset token — token is logged to server console")
    @PostMapping("/forgot-password")
    public ResponseEntity<MessageResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (user == null) {
            log.info("Forgot-password: email not found (not disclosed to client): {}", request.getEmail());
            return ResponseEntity.ok(new MessageResponse("If this email is registered, a reset token has been generated."));
        }

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token).user(user)
                .expiresAt(LocalDateTime.now().plusHours(1))
                .used(false)
                .build();
        passwordResetTokenRepository.save(resetToken);

        // Send Reset Email
        emailService.sendPasswordResetEmail(user.getEmail(), token);

        // ===== ALSO LOG FOR DEVELOPMENT PURPOSES =====
        log.info("=== PASSWORD RESET TOKEN for [{}] === TOKEN: [{}] ===", request.getEmail(), token);

        return ResponseEntity.ok(new MessageResponse("If this email is registered, a reset token has been generated."));
    }

    @Operation(summary = "Reset password using a valid token")
    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        PasswordResetToken resetToken = passwordResetTokenRepository
                .findByToken(request.getToken()).orElse(null);

        if (resetToken == null || resetToken.isUsed()) {
            log.warn("Password reset REJECTED — invalid or already-used token");
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid or already used token."));
        }
        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            log.warn("Password reset REJECTED — expired token for user: {}", resetToken.getUser().getEmail());
            return ResponseEntity.badRequest().body(new MessageResponse("Token has expired. Request a new one."));
        }

        User user = resetToken.getUser();
        // Encode ONCE — raw password → $2a$10$... — never re-encode an already encoded
        // hash
        String newHash = passwordEncoder.encode(request.getNewPassword());
        log.info("Password reset SUCCESS for: {} | BCrypt prefix: {}", user.getEmail(),
                newHash.substring(0, Math.min(7, newHash.length())));

        user.setPassword(newHash);
        userRepository.save(user);

        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);

        return ResponseEntity.ok(new MessageResponse("Password reset successfully. You can now log in."));
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
