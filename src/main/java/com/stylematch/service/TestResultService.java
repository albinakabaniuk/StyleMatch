package com.stylematch.service;

import com.stylematch.domain.TestResult;
import com.stylematch.domain.User;
import com.stylematch.dto.ProfileResultsResponse;
import com.stylematch.dto.TestResultRequest;
import com.stylematch.dto.TestResultResponse;
import com.stylematch.repository.TestResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestResultService {

    private static final Logger log = LoggerFactory.getLogger(TestResultService.class);
    private final TestResultRepository testResultRepository;

    public TestResultService(TestResultRepository testResultRepository) {
        this.testResultRepository = testResultRepository;
    }

    @Transactional
    public TestResultResponse saveResult(User user, TestResultRequest request) {
        TestResult testResult = TestResult.builder()
                .user(user)
                .testType(request.getTestType())
                .result(request.getResult())
                .metadata(request.getMetadata())
                .summary(request.getSummary())
                .build();

        TestResult saved = testResultRepository.save(testResult);
        log.info("Saved {} test result for user: {}", request.getTestType(), user.getEmail());
        return mapToResponse(saved);
    }

    public List<TestResultResponse> getMyResults(User user) {
        return testResultRepository.findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<TestResultResponse> getMyResultsByType(User user, String type) {
        return testResultRepository.findByUserAndTestTypeOrderByCreatedAtDesc(user, type)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ProfileResultsResponse getGroupedResults(User user) {
        List<TestResultResponse> all = getMyResults(user);
        
        List<TestResultResponse> colorResults = all.stream()
                .filter(r -> "COLOR_TYPE".equals(r.getTestType()) || "COLOR_TEST".equals(r.getTestType()) || "COLOR".equals(r.getTestType()) || "PHOTO_ANALYSIS".equals(r.getTestType()))
                .collect(Collectors.toList());
        
        List<TestResultResponse> bodyResults = all.stream()
                .filter(r -> "BODY_SHAPE".equals(r.getTestType()))
                .collect(Collectors.toList());

        return ProfileResultsResponse.builder()
                .colorTypeResults(colorResults)
                .bodyShapeResults(bodyResults)
                .build();
    }

    @Transactional
    public void deleteResult(User user, Long id) {
        TestResult result = testResultRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("Result not found or unauthorized"));
        testResultRepository.delete(result);
        log.info("Deleted test result {} for user: {}", id, user.getEmail());
    }

    @Transactional
    public void deleteAllResults(User user) {
        testResultRepository.deleteByUser(user);
        log.info("Deleted all test results for user: {}", user.getEmail());
    }

    @Transactional
    public void deleteResultsByType(User user, String type) {
        if ("COLOR_TYPE".equals(type)) {
            // Bulk delete all color-related results
            testResultRepository.deleteByUserAndTestType(user, "COLOR_TYPE");
            testResultRepository.deleteByUserAndTestType(user, "PHOTO_ANALYSIS");
            testResultRepository.deleteByUserAndTestType(user, "COLOR");
            testResultRepository.deleteByUserAndTestType(user, "COLOR_TEST");
        } else {
            testResultRepository.deleteByUserAndTestType(user, type);
        }
        log.info("Deleted {} test results for user: {}", type, user.getEmail());
    }

    private TestResultResponse mapToResponse(TestResult entity) {
        return TestResultResponse.builder()
                .id(entity.getId())
                .testType(entity.getTestType())
                .result(entity.getResult())
                .metadata(entity.getMetadata())
                .summary(entity.getSummary())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
