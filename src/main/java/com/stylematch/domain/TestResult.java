package com.stylematch.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "test_results")
public class TestResult {

    public TestResult() {}

    public TestResult(Long id, User user, String testType, String result, String metadata, LocalDateTime createdAt, String resultValue, String summary) {
        this.id = id;
        this.user = user;
        this.testType = testType;
        this.result = result;
        this.metadata = metadata;
        this.createdAt = createdAt;
        this.resultValue = resultValue;
        this.summary = summary;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "test_type", nullable = false)
    private String testType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String result;

    @Column(columnDefinition = "TEXT")
    private String metadata;

    @Transient
    private String resultValue;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getTestType() { return testType; }
    public void setTestType(String testType) { this.testType = testType; }
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
    public String getMetadata() { return metadata; }
    public void setMetadata(String metadata) { this.metadata = metadata; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getResultValue() { return resultValue != null ? resultValue : result; }
    public void setResultValue(String value) { this.resultValue = value; this.result = value; }
    public String getSummary() { return summary != null ? summary : metadata; }
    public void setSummary(String summary) { this.summary = summary; }

    // Manual builder
    public static class TestResultBuilder {
        private Long id;
        private User user;
        private String testType;
        private String result;
        private String metadata;
        private LocalDateTime createdAt;
        private String resultValue;
        private String summary;

        public TestResultBuilder id(Long id) { this.id = id; return this; }
        public TestResultBuilder user(User user) { this.user = user; return this; }
        public TestResultBuilder testType(String testType) { this.testType = testType; return this; }
        public TestResultBuilder result(String result) { this.result = result; return this; }
        public TestResultBuilder metadata(String metadata) { this.metadata = metadata; return this; }
        public TestResultBuilder resultValue(String resultValue) { this.resultValue = resultValue; this.result = resultValue; return this; }
        public TestResultBuilder summary(String summary) { this.summary = summary; return this; }
        public TestResultBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public TestResult build() {
            return new TestResult(id, user, testType, result, metadata, createdAt, resultValue, summary);
        }
    }

    public static TestResultBuilder builder() {
        return new TestResultBuilder();
    }
}
