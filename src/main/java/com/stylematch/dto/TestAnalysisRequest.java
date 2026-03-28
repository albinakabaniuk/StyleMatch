package com.stylematch.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@Schema(description = "Request payload for test-based style analysis")
public class TestAnalysisRequest {

    public TestAnalysisRequest() {}

    public TestAnalysisRequest(List<TestAnswer> answers, String language) {
        this.answers = answers;
        this.language = language;
    }

    public static TestAnalysisRequestBuilder builder() {
        return new TestAnalysisRequestBuilder();
    }

    public static class TestAnalysisRequestBuilder {
        private List<TestAnswer> answers;
        private String language;

        public TestAnalysisRequestBuilder answers(List<TestAnswer> answers) { this.answers = answers; return this; }
        public TestAnalysisRequestBuilder language(String language) { this.language = language; return this; }

        public TestAnalysisRequest build() {
            return new TestAnalysisRequest(answers, language);
        }
    }

    @Schema(description = "List of answers from the style questionnaire")
    @NotEmpty(message = "Answers list cannot be empty")
    @Valid
    private List<TestAnswer> answers;

    @Schema(description = "The selected language for the analysis results", example = "en")
    private String language;

    public List<TestAnswer> getAnswers() { return answers; }
    public void setAnswers(List<TestAnswer> answers) { this.answers = answers; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    @Override
    public String toString() {
        return "TestAnalysisRequest(answers=" + answers + ", language=" + language + ")";
    }
}
