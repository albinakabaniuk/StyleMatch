package com.stylematch.controller;

import com.stylematch.domain.User;
import com.stylematch.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin Utilities", description = "Endpoints for database migrations and maintenance")
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(summary = "Migrate plain-text passwords", description = "Finds all users with plain-text passwords (not starting with standard BCrypt prefixes) and securely hashes them.")
    @ApiResponse(responseCode = "200", description = "Migration completed successfully with a count of updated rows")
    @PostMapping("/migrate-passwords")
    public ResponseEntity<String> migratePasswords() {
        log.info("REST request to start password migration utility...");
        List<User> users = userRepository.findAll();
        int migratedCount = 0;

        for (User user : users) {
            String currentPassword = user.getPassword();
            // Standard BCrypt hashes typically start with $2a$, $2b$, or $2y$ and are 60
            // characters long.
            if (currentPassword != null && !currentPassword.startsWith("$2a$")
                    && !currentPassword.startsWith("$2b$")
                    && !currentPassword.startsWith("$2y$")) {

                log.info("Migrating plain-text password for user securely: {}", user.getEmail());
                user.setPassword(passwordEncoder.encode(currentPassword));
                userRepository.save(user);
                migratedCount++;
            }
        }

        log.info("Migration complete. Securely migrated {} passwords to BCrypt.", migratedCount);
        return ResponseEntity
                .ok("Successfully securely migrated " + migratedCount + " legacy plain-text passwords to BCrypt.");
    }
}
