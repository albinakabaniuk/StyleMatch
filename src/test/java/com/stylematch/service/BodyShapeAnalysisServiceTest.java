package com.stylematch.service;

import com.stylematch.domain.BodyShape;
import com.stylematch.domain.User;
import com.stylematch.dto.BodyShapeResponse;
import com.stylematch.dto.BodyShapeTestRequest;
import com.stylematch.dto.TestAnswer;
import com.stylematch.repository.TestResultRepository;
import com.stylematch.domain.TestResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BodyShapeAnalysisServiceTest {

    @Mock
    private TestResultRepository testResultRepository;

    @Mock
    private AIService aiService;

    @Mock
    private ObjectMapper objectMapper;


    @InjectMocks
    private BodyShapeAnalysisService bodyShapeAnalysisService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(UUID.randomUUID())
                .email("test@example.com")
                .preferredLanguage("en")
                .build();
    }

    @Test
    void analyzeBodyShape_Hourglass_Success() {
        // Given: Answers predominantly 'C'
        BodyShapeTestRequest request = BodyShapeTestRequest.builder()
                .answers(List.of(
                    new TestAnswer(1, "C"),
                    new TestAnswer(2, "C"),
                    new TestAnswer(3, "C"),
                    new TestAnswer(4, "A")
                ))
                .language("en")
                .build();


        // When
        BodyShapeResponse response = bodyShapeAnalysisService.analyzeBodyShape(testUser, request);

        // Then
        assertEquals(BodyShape.HOURGLASS, response.getBodyShapeType());
        verify(testResultRepository).save(any(TestResult.class));
    }

    @Test
    void analyzeBodyShape_Pear_Success() {
        // Given: Answers predominantly 'A'
        BodyShapeTestRequest request = BodyShapeTestRequest.builder()
                .answers(List.of(
                    new TestAnswer(1, "A"),
                    new TestAnswer(2, "A"),
                    new TestAnswer(3, "A"),
                    new TestAnswer(4, "B")
                ))
                .language("en")
                .build();


        // When
        BodyShapeResponse response = bodyShapeAnalysisService.analyzeBodyShape(testUser, request);

        // Then
        assertEquals(BodyShape.PEAR, response.getBodyShapeType());
    }

    @Test
    void analyzeBodyShape_Apple_Default() {
        // Given: Empty answers or no majority
        BodyShapeTestRequest request = BodyShapeTestRequest.builder()
                .answers(List.of())
                .language("en")
                .build();


        // When
        BodyShapeResponse response = bodyShapeAnalysisService.analyzeBodyShape(testUser, request);

        // Then: Defaults to APPLE when all counts are 0
        assertEquals(BodyShape.APPLE, response.getBodyShapeType());
    }
}
