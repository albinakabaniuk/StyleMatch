package com.stylematch.service;

import com.stylematch.domain.User;
import com.stylematch.dto.UpdateUserProfileRequest;
import com.stylematch.dto.UserProfileResponse;
import com.stylematch.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String email = username.toLowerCase().trim();
        log.debug("UserDetailsService: loading user by email: {}", email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("UserDetailsService: user NOT FOUND for email: {}", email);
                    return new UsernameNotFoundException("User not found with email: " + email);
                });
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email.toLowerCase().trim());
    }

    public void changeLanguage(User user, String language) {
        user.setPreferredLanguage(language);
        userRepository.save(user);
        log.info("Language preference updated for user: {} -> {}", user.getEmail(), language);
    }

    public UserProfileResponse getUserProfile(User user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .age(user.getAge())
                .weight(user.getWeight())
                .height(user.getHeight())
                .language(user.getPreferredLanguage())
                .gender(user.getGender())
                .avatar(user.getAvatar())
                .clothingSize(user.getClothingSize())
                .build();
    }

    public UserProfileResponse getProfileByEmail(String email) {
        String normalizedEmail = email.toLowerCase().trim();
        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + normalizedEmail));
        return getUserProfile(user);
    }

    @Transactional
    public UserProfileResponse updateUserProfile(User authUser, UpdateUserProfileRequest request) {
        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        log.info("Updating profile for user: {}. Avatar length: {}, Clothing size: {}", 
            user.getEmail(), 
            request.getAvatar() != null ? request.getAvatar().length() : "null", 
            request.getClothingSize());

        if (request.getName() != null) user.setName(request.getName());
        // Handle email change first to ensure uniqueness check is done early
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("Email already in use");
            }
            user.setEmail(request.getEmail());
        }
        if (request.getAge() != null) user.setAge(request.getAge());
        if (request.getGender() != null) user.setGender(request.getGender());
        if (request.getWeight() != null) user.setWeight(request.getWeight());
        if (request.getHeight() != null) user.setHeight(request.getHeight());
        if (request.getPreferredLanguage() != null) user.setPreferredLanguage(request.getPreferredLanguage());
        if (request.getAvatar() != null) user.setAvatar(request.getAvatar());
        if (request.getClothingSize() != null) user.setClothingSize(request.getClothingSize());
        
        userRepository.save(user);
        log.info("Profile updated successfully for user: {}", user.getEmail());
        return getUserProfile(user);
    }

    @Transactional
    public void changePassword(User user, String currentPassword, String newPassword) {
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password does not match");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("Password changed for user: {}", user.getEmail());
        
        // Notify user via email
        emailService.sendPasswordChangedNotification(user.getEmail());
    }

    @Transactional
    public void deleteUserAccount(UUID userId) {
        userRepository.deleteById(userId);
        log.info("User account deleted: {}", userId);
    }
}
