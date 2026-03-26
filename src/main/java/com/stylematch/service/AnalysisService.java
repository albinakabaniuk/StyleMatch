package com.stylematch.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stylematch.domain.*;
import com.stylematch.dto.AIAnalysisResult;
import com.stylematch.dto.AnalysisResponse;
import com.stylematch.dto.TestAnalysisRequest;
import com.stylematch.dto.TestAnswer;
import com.stylematch.dto.TestResultRequest;
import com.stylematch.repository.AnalysisAnswerRepository;
import com.stylematch.repository.AnalysisResultRepository;
import com.stylematch.repository.TestResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class AnalysisService {

    private static final Logger log = LoggerFactory.getLogger(AnalysisService.class);

    private final AnalysisAnswerRepository answerRepository;
    private final AnalysisResultRepository analysisResultRepository;
    private final TestResultRepository testResultRepository;
    private final AIService aiService;
    private final ObjectMapper objectMapper;

    public AnalysisService(AnalysisAnswerRepository answerRepository, 
                           AnalysisResultRepository analysisResultRepository, 
                           TestResultRepository testResultRepository, 
                           AIService aiService,
                           ObjectMapper objectMapper) {
        this.answerRepository = answerRepository;
        this.analysisResultRepository = analysisResultRepository;
        this.testResultRepository = testResultRepository;
        this.aiService = aiService;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public AnalysisResponse analyzeTest(User user, TestAnalysisRequest request) {
        long start = System.currentTimeMillis();
        log.info("[PERF] analyzeTest start — user={}, answers={}", user != null ? user.getEmail() : "anonymous", request.getAnswers().size());

        List<TestAnswer> answers = request.getAnswers();
        
        // Prepare context for AI
        Map<String, String> answerMap = answers.stream()
                .collect(Collectors.toMap(
                        ans -> String.valueOf(ans.getQuestionId()),
                        TestAnswer::getAnswer,
                        (v1, v2) -> v1
                ));
        log.info("Built answerMap for COLOR_TYPE: {}", answerMap);

        // Call AI Service with isolation
        String lang = request.getLanguage() != null ? request.getLanguage() : user.getPreferredLanguage();
        if (lang == null) lang = "en";
        
        AIAnalysisResult aiResult;
        ColorType colorType;
        Undertone undertone;
        ContrastLevel contrast;

        try {
            // DEBUG: Force failure by setting -DsimulateAIFailure=true in JVM options
            if (Boolean.getBoolean("simulateAIFailure")) {
                throw new RuntimeException("Simulated AI Failure");
            }
            aiResult = aiService.analyzeFashion(user, "COLOR_TYPE", answerMap, lang);
            colorType = ColorType.valueOf(aiResult.getResultType());
            undertone = Undertone.valueOf(aiResult.getUndertone());
            contrast = ContrastLevel.valueOf(aiResult.getContrastLevel());
        } catch (Exception e) {
            log.error("AI Analysis failed for user {}: {}. Using fallbacks.", user != null ? user.getEmail() : "unknown", e.getMessage());
            // Fallback to a safe default (e.g., SUMMER/COOL/MEDIUM or BASED ON ANSWERS if logic existed)
            colorType = ColorType.SUMMER; 
            undertone = Undertone.COOL;
            contrast = ContrastLevel.MEDIUM;
            aiResult = AIAnalysisResult.builder()
                    .resultType(colorType.name())
                    .undertone(undertone.name())
                    .contrastLevel(contrast.name())
                    .summary("result.aiSummaryFallback")
                    .palette(getPaletteFallback(colorType))
                    .recommendations(List.of("Try re-running analysis later for personalized tips."))
                    .personalizedAdvice("result.aiOffline")
                    .build();
        }

        // Create the core AnalysisResult
        AnalysisResult result = AnalysisResult.builder()
                .user(user)
                .inputType("TEST")
                .colorType(colorType)
                .undertone(undertone)
                .contrastLevel(contrast)
                .depth(aiResult.getDepth())
                .chroma(aiResult.getChroma())
                .rawData("AI Summary: " + aiResult.getSummary())
                .build();
        
        // ... (rest of the mapping code for answers)
        List<AnalysisAnswer> domainAnswers = new ArrayList<>(answers.size());
        for (TestAnswer ans : answers) {
            domainAnswers.add(AnalysisAnswer.builder()
                    .analysisResult(result)
                    .questionId(ans.getQuestionId())
                    .answerValue(ans.getAnswer())
                    .build());
        }
        result.setAnswers(domainAnswers);

        if (user != null) {
            analysisResultRepository.save(result);            // Save to generic TestResult persistence
            try {
                Map<String, Object> metadataMap = new HashMap<>();
                metadataMap.put("undertone", undertone.name());
                metadataMap.put("contrast", contrast.name());
                metadataMap.put("season", aiResult.getSeason() != null ? aiResult.getSeason() : "N/A");
                metadataMap.put("depth", aiResult.getDepth() != null ? aiResult.getDepth() : "N/A");
                metadataMap.put("chroma", aiResult.getChroma() != null ? aiResult.getChroma() : "N/A");
                metadataMap.put("palette", aiResult.getPalette() != null ? aiResult.getPalette() : List.of());
                metadataMap.put("summary", aiResult.getSummary());
                metadataMap.put("advice", aiResult.getPersonalizedAdvice());
                
                String jsonMetadata = objectMapper.writeValueAsString(metadataMap);
                
                TestResult testResult = TestResult.builder()
                        .user(user)
                        .testType("COLOR_TYPE")
                        .result(undertone.name() + "_" + colorType.name())
                        .metadata(jsonMetadata)
                        .summary(aiResult.getSummary())
                        .build();
                testResultRepository.save(testResult);
            } catch (Exception e) {
                log.error("Failed to save ColorType history for user {}: {}", user.getEmail(), e.getMessage());
            }
        }

        log.info("[PERF] Analysis completed for user={} (Total time: {}ms)", 
                user != null ? user.getEmail() : "anonymous", System.currentTimeMillis() - start);

        return AnalysisResponse.builder()
                .colorType(colorType)
                .undertone(undertone)
                .contrastLevel(contrast)
                .depth(aiResult.getDepth())
                .chroma(aiResult.getChroma())
                .season(aiResult.getSeason() != null ? aiResult.getSeason() : colorType.name())
                .message(aiResult.getSummary())
                .palette(aiResult.getPalette() != null && !aiResult.getPalette().isEmpty() ? aiResult.getPalette() : getPaletteFallback(colorType))
                .personalizedAdvice(aiResult.getPersonalizedAdvice())
                .aiResult(aiResult)
                .build();
    }

    private List<String> getPaletteFallback(ColorType type) {
        return switch (type) {
            case DEEP_WINTER -> List.of("#000000", "#1a1a1a", "#002366", "#004d40", "#4a0e0e", "#ffffff");
            case COOL_WINTER -> List.of("#0047ab", "#ff007f", "#ffffff", "#e5e4e2", "#36454f", "#000080");
            case BRIGHT_WINTER -> List.of("#ff00ff", "#1f51ff", "#000000", "#ffffff", "#ffff00", "#00ff00");
            case LIGHT_SUMMER -> List.of("#add8e6", "#ffb6c1", "#e6e6fa", "#f5f5f5", "#98fb98", "#ffdae0");
            case COOL_SUMMER -> List.of("#80daeb", "#778899", "#bc8f8f", "#967117", "#9370db", "#f0f8ff");
            case SOFT_SUMMER -> List.of("#5d3954", "#4e5d6c", "#8a624a", "#a18e8d", "#d3d3d3", "#36454f");
            case LIGHT_SPRING -> List.of("#ffe5b4", "#fbceb1", "#ffffed", "#ff7f50", "#fffdd0", "#fffafa");
            case WARM_SPRING -> List.of("#fb8e7e", "#ffdb58", "#98fb98", "#c19a6b", "#fffaf0", "#f4a460");
            case BRIGHT_SPRING -> List.of("#00ced1", "#ff1493", "#ffd700", "#ff8c00", "#32cd32", "#ffffff");
            case DEEP_AUTUMN -> List.of("#2e1503", "#8b4513", "#556b2f", "#ff8c00", "#d2691e", "#3d2b1f");
            case WARM_AUTUMN -> List.of("#e2725b", "#808000", "#daa520", "#b87333", "#cc7722", "#ffefd5");
            case SOFT_AUTUMN -> List.of("#fa8072", "#f0e68c", "#808b96", "#eae0c8", "#a0a0a0", "#414a4c");
            case WINTER -> List.of("#002366", "#000000", "#ffffff", "#ff007f");
            case SUMMER -> List.of("#add8e6", "#778899", "#f5f5f5", "#9370db");
            case SPRING -> List.of("#ffe5b4", "#ff7f50", "#ffd700", "#ffffff");
            case AUTUMN -> List.of("#8b4513", "#556b2f", "#daa520", "#2e1503");
            default -> List.of("#581c87", "#7c3aed", "#a855f7", "#c084fc", "#e879f9", "#fdf4ff");
        };
    }

    @Transactional
    public AnalysisResponse analyzePhoto(User user, MultipartFile file, String language) {
        long start = System.currentTimeMillis();
        log.info("[PERF] analyzePhoto start — file={}, user={}, lang={}", file.getOriginalFilename(), user != null ? user.getEmail() : "anonymous", language);

        String lang = language != null ? language : user.getPreferredLanguage();
        if (lang == null) lang = "en";

        // Call AI Service (Ideally with vision, but for now we follow the same hardened JSON prompt)
        Map<String, String> photoContext = new HashMap<>();
        photoContext.put("inputType", "PHOTO");
        photoContext.put("fileName", file.getOriginalFilename());

        AIAnalysisResult aiResult;
        ColorType colorType;
        Undertone undertone;
        ContrastLevel contrast;

        try {
            aiResult = aiService.analyzeFashion(user, "COLOR_TYPE", photoContext, lang);
            colorType = ColorType.valueOf(aiResult.getResultType());
            undertone = Undertone.valueOf(aiResult.getUndertone());
            contrast = ContrastLevel.valueOf(aiResult.getContrastLevel());
        } catch (Exception e) {
            log.error("AI Photo Analysis failed for user {}: {}. Using fallbacks.", user != null ? user.getEmail() : "anonymous", e.getMessage());
            colorType = ColorType.AUTUMN;
            undertone = Undertone.WARM;
            contrast = ContrastLevel.MEDIUM;
            aiResult = AIAnalysisResult.builder()
                    .resultType(colorType.name())
                    .undertone(undertone.name())
                    .contrastLevel(contrast.name())
                    .season("EARTHY_AUTUMN")
                    .summary("Photo analysis fallback triggered.")
                    .palette(getPaletteFallback(colorType))
                    .recommendations(List.of("Try uploading a clearer photo."))
                    .personalizedAdvice("Wear earthy tones that complement your warm autumn palette.")
                    .build();
        }

        AnalysisResult result = AnalysisResult.builder()
                .user(user)
                .inputType("PHOTO")
                .colorType(colorType)
                .undertone(undertone)
                .contrastLevel(contrast)
                .rawData("File: " + file.getOriginalFilename())
                .build();

        analysisResultRepository.save(result);
        
        // Save to generic TestResult persistence
        try {
            Map<String, Object> metadataMap = new HashMap<>();
            metadataMap.put("undertone", undertone.name());
            metadataMap.put("contrast", contrast.name());
            metadataMap.put("season", aiResult.getSeason() != null ? aiResult.getSeason() : "N/A");
            metadataMap.put("palette", aiResult.getPalette() != null ? aiResult.getPalette() : List.of());
            metadataMap.put("summary", aiResult.getSummary());
            metadataMap.put("advice", aiResult.getPersonalizedAdvice());
            
            String jsonMetadata = objectMapper.writeValueAsString(metadataMap);

            testResultRepository.save(TestResult.builder()
                    .user(user)
                    .testType("COLOR_TYPE")
                    .result(undertone.name() + "_" + colorType.name())
                    .metadata(jsonMetadata)
                    .build());
        } catch (Exception e) {
            log.error("Failed to save TestResult history for photo analysis: {}", e.getMessage());
        }

        log.info("[PERF] analyzePhoto total={}ms", System.currentTimeMillis() - start);

        return AnalysisResponse.builder()
                .colorType(colorType)
                .undertone(undertone)
                .contrastLevel(contrast)
                .season(aiResult.getSeason() != null ? aiResult.getSeason() : colorType.name())
                .message(aiResult.getSummary())
                .palette(aiResult.getPalette() != null && !aiResult.getPalette().isEmpty() ? aiResult.getPalette() : getPaletteFallback(colorType))
                .personalizedAdvice(aiResult.getPersonalizedAdvice())
                .aiResult(aiResult)
                .build();
    }
}
