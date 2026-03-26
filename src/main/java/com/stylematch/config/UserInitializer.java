package com.stylematch.config;

import com.stylematch.domain.Role;
import com.stylematch.domain.User;
import com.stylematch.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserInitializer {

    private static final Logger log = LoggerFactory.getLogger(UserInitializer.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public CommandLineRunner initUser() {
        return args -> {
            if (userRepository.findByEmail("user@example.com").isEmpty()) {
                log.info("Creating default test user: user@example.com / password123");
                User user = User.builder()
                        .email("user@example.com")
                        .password(passwordEncoder.encode("password123"))
                        .name("Test User")
                        .role(Role.USER)
                        .preferredLanguage("en")
                        .build();
                userRepository.save(user);
            }
        };
    }
}
