package com.stylematch.controller;

import com.stylematch.domain.User;
import com.stylematch.dto.AnalysisResponse;
import com.stylematch.dto.TestAnalysisRequest;
import com.stylematch.service.AnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/analysis")
@Tag(name = "Style Analysis", description = "Endpoints for deriving style attributes using AI and tests")
public class AnalysisController {

        private static final Logger log = LoggerFactory.getLogger(AnalysisController.class);
        private final AnalysisService analysisService;

        public AnalysisController(AnalysisService analysisService) {
                this.analysisService = analysisService;
        }

        @Operation(summary = "Analyze test answers", description = "Processes a predefined questionnaire to determine style attributes.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Analysis completed successfully"),
                        @ApiResponse(responseCode = "400", description = "Invalid request payload")
        })
        @PostMapping("/test")
        public ResponseEntity<AnalysisResponse> analyzeTest(
                        @AuthenticationPrincipal User user,
                        @Parameter(description = "Test answers payload") @Valid @RequestBody TestAnalysisRequest request) {
                long start = System.currentTimeMillis();
                log.info("[PERF] POST /api/analysis/test — user={}",
                                user != null ? user.getEmail() : "anonymous");
                log.info("Incoming TestAnalysisRequest: {}", request);
                AnalysisResponse response = analysisService.analyzeTest(user, request);
                log.info("[PERF] POST /api/analysis/test total={}ms", System.currentTimeMillis() - start);
                return ResponseEntity.ok(response);
        }

        @Operation(summary = "Analyze uploaded photo", description = "Processes a user's photo using an AI vision model (mocked) to determine attributes.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Photo analyzed successfully"),
                        @ApiResponse(responseCode = "400", description = "Invalid file upload")
        })

        @PostMapping(value = "/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<AnalysisResponse> analyzePhoto(
                        @AuthenticationPrincipal User user,
                        @Parameter(description = "The photo file to analyze") @RequestPart("file") MultipartFile file,
                        @RequestParam(value = "language", required = false) String language) {
                long start = System.currentTimeMillis();
                log.info("[PERF] POST /api/analysis/photo — file={}, user={}, lang={}",
                                file.getOriginalFilename(), user != null ? user.getEmail() : "anonymous", language);
                AnalysisResponse response = analysisService.analyzePhoto(user, file, language);
                log.info("[PERF] POST /api/analysis/photo total={}ms", System.currentTimeMillis() - start);
                return ResponseEntity.ok(response);
        }
}
