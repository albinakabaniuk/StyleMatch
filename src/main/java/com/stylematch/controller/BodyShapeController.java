package com.stylematch.controller;

import com.stylematch.domain.User;
import com.stylematch.dto.BodyShapeResponse;
import com.stylematch.dto.BodyShapeTestRequest;
import com.stylematch.service.BodyShapeAnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/body-shape")
@Tag(name = "Body Shape", description = "Endpoints for body shape analysis test.")
@SecurityRequirement(name = "bearerAuth")
public class BodyShapeController {

    private final BodyShapeAnalysisService bodyShapeAnalysisService;

    public BodyShapeController(BodyShapeAnalysisService bodyShapeAnalysisService) {
        this.bodyShapeAnalysisService = bodyShapeAnalysisService;
    }

    @PostMapping("/analyze")
    @Operation(summary = "Analyze body shape", description = "Processes test answers to determine body shape and returns recommendations.")
    public ResponseEntity<BodyShapeResponse> analyzeBodyShape(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody BodyShapeTestRequest request) {
        return ResponseEntity.ok(bodyShapeAnalysisService.analyzeBodyShape(user, request));
    }
}
