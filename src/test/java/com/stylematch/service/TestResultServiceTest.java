package com.stylematch.service;

import com.stylematch.domain.TestResult;
import com.stylematch.domain.User;
import com.stylematch.dto.ProfileResultsResponse;
import com.stylematch.dto.TestResultRequest;
import com.stylematch.dto.TestResultResponse;
import com.stylematch.repository.TestResultRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestResultServiceTest {

    @Mock
    private TestResultRepository testResultRepository;

    @InjectMocks
    private TestResultService testResultService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(UUID.randomUUID())
                .email("test@example.com")
                .build();
    }

    @Test
    void saveResult_Success() {
        // Given
        TestResultRequest request = new TestResultRequest("COLOR_TYPE", "Summer", "Metadata", "Summary");
        TestResult entity = TestResult.builder()
                .id(100L)
                .testType("COLOR_TYPE")
                .result("Summer")
                .metadata("Metadata")
                .createdAt(LocalDateTime.now())
                .build();
        when(testResultRepository.save(any(TestResult.class))).thenReturn(entity);

        // When
        TestResultResponse response = testResultService.saveResult(testUser, request);

        // Then
        assertNotNull(response);
        assertEquals("COLOR_TYPE", response.getTestType());
        verify(testResultRepository).save(any(TestResult.class));
    }

    @Test
    void getGroupedResults_Success() {
        // Given
        TestResult r1 = TestResult.builder().testType("COLOR_TYPE").result("Winter").createdAt(LocalDateTime.now()).build();
        TestResult r2 = TestResult.builder().testType("BODY_SHAPE").result("Triangle").createdAt(LocalDateTime.now()).build();
        
        when(testResultRepository.findByUserOrderByCreatedAtDesc(testUser)).thenReturn(List.of(r1, r2));

        // When
        ProfileResultsResponse response = testResultService.getGroupedResults(testUser);

        // Then
        assertEquals(1, response.getColorTypeResults().size());
        assertEquals(1, response.getBodyShapeResults().size());
        assertEquals("Winter", response.getColorTypeResults().get(0).getResult());
        assertEquals("Triangle", response.getBodyShapeResults().get(0).getResult());
    }

    @Test
    void deleteResult_Success() {
        // Given
        Long resultId = 100L;
        TestResult entity = TestResult.builder().id(resultId).user(testUser).build();
        when(testResultRepository.findByIdAndUser(resultId, testUser)).thenReturn(Optional.of(entity));

        // When
        testResultService.deleteResult(testUser, resultId);

        // Then
        verify(testResultRepository).delete(entity);
    }

    @Test
    void deleteResult_NotFoundOrUnauthorized() {
        // Given
        when(testResultRepository.findByIdAndUser(anyLong(), any())).thenReturn(Optional.empty());

        // When/Then
        assertThrows(IllegalArgumentException.class, () -> {
            testResultService.deleteResult(testUser, 999L);
        });
        verify(testResultRepository, never()).delete(any());
    }

    @Test
    void deleteAllResults_Success() {
        // When
        testResultService.deleteAllResults(testUser);

        // Then
        verify(testResultRepository).deleteByUser(testUser);
    }
}
