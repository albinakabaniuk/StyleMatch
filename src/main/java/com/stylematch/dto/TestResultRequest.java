package com.stylematch.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotBlank;

public class TestResultRequest {

    public TestResultRequest() {}

    public TestResultRequest(String testType, String result, String metadata, String summary) {
        this.testType = testType;
        this.result = result;
        this.metadata = metadata;
        this.summary = summary;
    }

    @NotBlank(message = "Test type is required")
    private String testType;

    @NotBlank(message = "Result is required")
    private String result;

    private String metadata;
    private String summary;

    public String getTestType() { return testType; }
    public void setTestType(String testType) { this.testType = testType; }
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
    public String getMetadata() { return metadata; }
    public void setMetadata(String metadata) { this.metadata = metadata; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    // Manual builder
    public static class TestResultRequestBuilder {
        private String testType;
        private String result;
        private String metadata;
        private String summary;

        public TestResultRequestBuilder testType(String testType) { this.testType = testType; return this; }
        public TestResultRequestBuilder result(String result) { this.result = result; return this; }
        public TestResultRequestBuilder metadata(String metadata) { this.metadata = metadata; return this; }
        public TestResultRequestBuilder summary(String summary) { this.summary = summary; return this; }

        public TestResultRequest build() {
            return new TestResultRequest(testType, result, metadata, summary);
        }
    }

    public static TestResultRequestBuilder builder() {
        return new TestResultRequestBuilder();
    }
}
