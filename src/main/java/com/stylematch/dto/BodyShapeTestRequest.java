package com.stylematch.dto;

import java.util.List;

public class BodyShapeTestRequest {
    public BodyShapeTestRequest() {}
    public BodyShapeTestRequest(List<TestAnswer> answers, String language) {
        this.answers = answers;
        this.language = language;
    }

    private List<TestAnswer> answers;
    private String language;

    public List<TestAnswer> getAnswers() { return answers; }
    public void setAnswers(List<TestAnswer> answers) { this.answers = answers; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    // Manual builder
    public static class BodyShapeTestRequestBuilder {
        private List<TestAnswer> answers;
        private String language;

        public BodyShapeTestRequestBuilder answers(List<TestAnswer> answers) { this.answers = answers; return this; }
        public BodyShapeTestRequestBuilder language(String language) { this.language = language; return this; }

        public BodyShapeTestRequest build() {
            return new BodyShapeTestRequest(answers, language);
        }
    }

    public static BodyShapeTestRequestBuilder builder() {
        return new BodyShapeTestRequestBuilder();
    }
}
