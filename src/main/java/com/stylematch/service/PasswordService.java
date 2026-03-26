package com.stylematch.service;

import com.stylematch.domain.User;
import com.stylematch.dto.ChangePasswordRequest;
import com.stylematch.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PasswordService {

    private static final Logger log = LoggerFactory.getLogger(PasswordService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void changePassword(User user, ChangePasswordRequest request) {
        log.info("Password change requested for user: {}", user.getEmail());

        // Verify current password against BCrypt hash — NO double encoding
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            log.warn("Password change REJECTED for: {} — current password mismatch", user.getEmail());
            throw new IllegalArgumentException("Incorrect current password");
        }

        // Encode the NEW password before storing — raw password never stored
        String newHash = passwordEncoder.encode(request.getNewPassword());
        log.info("Password changed successfully for: {} | new hash prefix: {}",
                user.getEmail(), newHash.substring(0, Math.min(7, newHash.length())));

        user.setPassword(newHash);
        userRepository.save(user);
    }
}
