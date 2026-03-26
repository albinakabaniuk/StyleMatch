package com.stylematch.service;

import com.stylematch.domain.Role;
import com.stylematch.domain.User;
import com.stylematch.dto.AuthRequest;
import com.stylematch.dto.AuthResponse;
import com.stylematch.dto.RegisterRequest;
import com.stylematch.repository.UserRepository;
import com.stylematch.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, UserService userService,
                      PasswordEncoder passwordEncoder, JwtUtil jwtUtil,
                      AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        long start = System.currentTimeMillis();
        log.info("[PERF] register start — email={}", request.getEmail());

        if (userService.existsByEmail(request.getEmail())) {
            log.warn("Registration attempt with already-registered email: {}", request.getEmail());
            throw new IllegalArgumentException("Email is already registered");
        }

        // BCrypt encoding is intentionally slow (~200ms) — this is the expected cost
        long bcryptStart = System.currentTimeMillis();
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        log.info("[PERF] BCrypt encode (register) took {}ms", System.currentTimeMillis() - bcryptStart);

        var user = User.builder()
                .email(request.getEmail().toLowerCase().trim())
                .password(encodedPassword)
                .role(Role.USER)
                .build();

        long dbStart = System.currentTimeMillis();
        userService.save(user);
        log.info("[PERF] DB user save took {}ms", System.currentTimeMillis() - dbStart);

        var jwtToken = jwtUtil.generateToken(user);
        log.info("[PERF] register total={}ms for user={}", System.currentTimeMillis() - start, request.getEmail());
        return AuthResponse.builder().token(jwtToken).build();
    }

    public AuthResponse login(AuthRequest request) {
        long start = System.currentTimeMillis();
        String email = request.getEmail().toLowerCase().trim();
        log.info("[AUTH] Login attempt — email={}", email);

        try {
            long bcryptStart = System.currentTimeMillis();
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, request.getPassword()));
            
            log.info("[PERF] BCrypt authenticate took {}ms", System.currentTimeMillis() - bcryptStart);
            
            // principal is already loaded and verified by authenticationManager
            var userDetails = (UserDetails) authentication.getPrincipal();
            var jwtToken = jwtUtil.generateToken(userDetails);
            
            log.info("[AUTH] Login SUCCESS: {}, total={}ms", email, System.currentTimeMillis() - start);
            return AuthResponse.builder().token(jwtToken).build();
            
        } catch (BadCredentialsException e) {
            log.warn("[AUTH] Login FAILED (invalid password) for={}, total={}ms", email, System.currentTimeMillis() - start);
            throw e;
        } catch (AuthenticationException e) {
            log.warn("[AUTH] Login FAILED (other auth error: {}) for={}, total={}ms", e.getMessage(), email, System.currentTimeMillis() - start);
            throw e;
        }
    }
}
