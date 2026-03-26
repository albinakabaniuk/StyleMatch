package com.stylematch.controller;

import com.stylematch.domain.User;
import com.stylematch.dto.ProfileResultsResponse;
import com.stylematch.dto.TestResultRequest;
import com.stylematch.dto.TestResultResponse;
import com.stylematch.service.TestResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test-results")
@Tag(name = "Test Results", description = "Endpoints for managing and retrieving user test history.")
@SecurityRequirement(name = "bearerAuth")
public class TestResultController {

    private final TestResultService testResultService;

    public TestResultController(TestResultService testResultService) {
        this.testResultService = testResultService;
    }

    @Operation(summary = "Get all test results grouped by type (Color Type vs Body Shape)")
    @GetMapping
    public ResponseEntity<ProfileResultsResponse> getGroupedResults(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(testResultService.getGroupedResults(user));
    }

    @Operation(summary = "Delete a specific test result by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResult(@AuthenticationPrincipal User user, @PathVariable Long id) {
        testResultService.deleteResult(user, id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete all test results for a specific user ID")
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteAllResultsByUser(@AuthenticationPrincipal User user, @PathVariable java.util.UUID userId) {
        // Simple security check: user can only delete their own data unless they are an admin
        // (Assuming User ID is UUID based on previous findings)
        if (!user.getId().equals(userId)) {
             return ResponseEntity.status(403).build();
        }
        testResultService.deleteAllResults(user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/save")
    @Operation(summary = "Internal: Save a test result", description = "Saves a generic test result for the current user.")
    public ResponseEntity<TestResultResponse> saveResult(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody TestResultRequest request) {
        return ResponseEntity.ok(testResultService.saveResult(user, request));
    }
}
