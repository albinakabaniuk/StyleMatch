package com.stylematch.dto;

import java.time.LocalDateTime;

public class TestResultResponse {
    public TestResultResponse() {}
    public TestResultResponse(Long id, String testType, String result, String metadata, LocalDateTime createdAt, String summary) {
        this.id = id;
        this.testType = testType;
        this.result = result;
        this.metadata = metadata;
        this.createdAt = createdAt;
        this.summary = summary;
    }
    private Long id;
    private String testType;
    private String result;
    private String metadata;
    private LocalDateTime createdAt;
    private String summary;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTestType() { return testType; }
    public void setTestType(String testType) { this.testType = testType; }
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
    public String getMetadata() { return metadata; }
    public void setMetadata(String metadata) { this.metadata = metadata; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    // Manual builder
    public static class TestResultResponseBuilder {
        private Long id;
        private String testType;
        private String result;
        private String metadata;
        private LocalDateTime createdAt;
        private String summary;

        public TestResultResponseBuilder id(Long id) { this.id = id; return this; }
        public TestResultResponseBuilder testType(String testType) { this.testType = testType; return this; }
        public TestResultResponseBuilder result(String result) { this.result = result; return this; }
        public TestResultResponseBuilder metadata(String metadata) { this.metadata = metadata; return this; }
        public TestResultResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public TestResultResponseBuilder summary(String summary) { this.summary = summary; return this; }

        public TestResultResponse build() {
            return new TestResultResponse(id, testType, result, metadata, createdAt, summary);
        }
    }

    public static TestResultResponseBuilder builder() {
        return new TestResultResponseBuilder();
    }
}
