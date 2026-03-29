package com.stylematch.controller;

import com.stylematch.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/debug")
@Tag(name = "Debug", description = "Endpoints for investigating system issues")
public class EmailDebugController {

    private final EmailService emailService;

    public EmailDebugController(EmailService emailService) {
        this.emailService = emailService;
    }

    @Operation(summary = "Test SMTP connectivity by sending a dummy reset email")
    @GetMapping("/test-email")
    public ResponseEntity<String> testEmail(@RequestParam String to) {
        try {
            String testToken = "DEBUG-TEST-TOKEN-" + Math.round(Math.random() * 10000);
            emailService.sendPasswordResetEmail(to, testToken);
            return ResponseEntity.ok("SUCCESS: Test email triggered for " + to + ". Check your inbox (and spam).");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("FAILURE: SMTP Error — " + e.getMessage());
        }
    }
}
