package com.stylematch.service;

import com.stylematch.domain.*;
import com.stylematch.dto.AIAnalysisResult;
import com.stylematch.dto.AnalysisResponse;
import com.stylematch.dto.TestAnalysisRequest;
import com.stylematch.dto.TestAnswer;
import com.stylematch.repository.AnalysisResultRepository;
import com.stylematch.repository.TestResultRepository;
import com.stylematch.domain.TestResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalysisServiceTest {

    @Mock
    private AnalysisResultRepository analysisResultRepository;

    @Mock
    private TestResultRepository testResultRepository;


    private AnalysisService analysisService;
    private ManualAIService manualAiService;
    private User testUser;

    // Manual mock for AIService to avoid Mockito-inline issues on certain JDKs
    private static class ManualAIService extends AIService {
        private AIAnalysisResult mockResult;

        public void setMockResult(AIAnalysisResult result) {
            this.mockResult = result;
        }

        @Override
        public AIAnalysisResult analyzeFashion(User user, String testType, Map<String, String> answerMap, String language) {
            return mockResult;
        }
    }

    @BeforeEach
    void setUp() {
        manualAiService = new ManualAIService();
        analysisService = new AnalysisService(
            analysisResultRepository,
            testResultRepository,
            manualAiService,
            new ObjectMapper()
        );

        testUser = User.builder()
                .id(UUID.randomUUID())
                .email("test@example.com")
                .name("Test User")
                .preferredLanguage("en")
                .build();
    }

    @Test
    void analyzeTest_Winter_Success() {
        // Given
        TestAnalysisRequest request = TestAnalysisRequest.builder()
                .answers(List.of(
                    new TestAnswer(1, "A"),
                    new TestAnswer(2, "A"),
                    new TestAnswer(3, "C"),
                    new TestAnswer(4, "C"),
                    new TestAnswer(5, "C")
                ))
                .language("en")
                .build();

        AIAnalysisResult aiResult = AIAnalysisResult.builder()
                .resultType("WINTER")
                .undertone("COOL")
                .contrastLevel("HIGH")
                .summary("Winter summary")
                .build();
        manualAiService.setMockResult(aiResult);

        // When
        AnalysisResponse response = analysisService.analyzeTest(testUser, request);

        // Then
        assertNotNull(response);
        assertEquals(ColorType.WINTER, response.getColorType());
        assertEquals(Undertone.COOL, response.getUndertone());
        assertEquals(ContrastLevel.HIGH, response.getContrastLevel());
        verify(analysisResultRepository).save(any(AnalysisResult.class));
        verify(testResultRepository).save(any(TestResult.class));
    }

    @Test
    void analyzeTest_Autumn_Success() {
        // Given
        TestAnalysisRequest request = TestAnalysisRequest.builder()
                .answers(List.of(
                    new TestAnswer(1, "B"),
                    new TestAnswer(2, "B"),
                    new TestAnswer(3, "C"),
                    new TestAnswer(4, "C"),
                    new TestAnswer(5, "C")
                ))
                .language("en")
                .build();

        AIAnalysisResult aiResult = AIAnalysisResult.builder()
                .resultType("AUTUMN")
                .undertone("WARM")
                .contrastLevel("HIGH")
                .summary("Autumn summary")
                .build();
        manualAiService.setMockResult(aiResult);

        // When
        AnalysisResponse response = analysisService.analyzeTest(testUser, request);

        // Then
        assertEquals(ColorType.AUTUMN, response.getColorType());
        assertEquals(Undertone.WARM, response.getUndertone());
        assertEquals(ContrastLevel.HIGH, response.getContrastLevel());
    }

    @Test
    void analyzeTest_EmptyAnswers() {
        // Given
        TestAnalysisRequest request = TestAnalysisRequest.builder()
                .answers(List.of())
                .language("en")
                .build();

        AIAnalysisResult aiResult = AIAnalysisResult.builder()
                .resultType("SPRING")
                .undertone("WARM")
                .contrastLevel("LOW")
                .summary("Spring summary")
                .build();
        manualAiService.setMockResult(aiResult);

        // When
        AnalysisResponse response = analysisService.analyzeTest(testUser, request);

        // Then
        assertEquals(ColorType.SPRING, response.getColorType());
    }

    @Test
    void analyzePhoto_Success() {
        // Given
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("face.jpg");

        // When
        AnalysisResponse response = analysisService.analyzePhoto(testUser, mockFile, "en");

        // Then
        assertNotNull(response);
        assertEquals(ColorType.AUTUMN, response.getColorType());
        verify(analysisResultRepository).save(any(AnalysisResult.class));
        verify(testResultRepository).save(any(TestResult.class));
    }
}
