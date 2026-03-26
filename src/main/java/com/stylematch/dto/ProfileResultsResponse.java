package com.stylematch.dto;

import java.util.List;

public class ProfileResultsResponse {
    public ProfileResultsResponse() {}
    public ProfileResultsResponse(List<TestResultResponse> colorTypeResults, List<TestResultResponse> bodyShapeResults) {
        this.colorTypeResults = colorTypeResults;
        this.bodyShapeResults = bodyShapeResults;
    }
    private List<TestResultResponse> colorTypeResults;
    private List<TestResultResponse> bodyShapeResults;

    public List<TestResultResponse> getColorTypeResults() { return colorTypeResults; }
    public void setColorTypeResults(List<TestResultResponse> colorTypeResults) { this.colorTypeResults = colorTypeResults; }
    public List<TestResultResponse> getBodyShapeResults() { return bodyShapeResults; }
    public void setBodyShapeResults(List<TestResultResponse> bodyShapeResults) { this.bodyShapeResults = bodyShapeResults; }

    // Manual builder
    public static class ProfileResultsResponseBuilder {
        private List<TestResultResponse> colorTypeResults;
        private List<TestResultResponse> bodyShapeResults;

        public ProfileResultsResponseBuilder colorTypeResults(List<TestResultResponse> colorTypeResults) { this.colorTypeResults = colorTypeResults; return this; }
        public ProfileResultsResponseBuilder bodyShapeResults(List<TestResultResponse> bodyShapeResults) { this.bodyShapeResults = bodyShapeResults; return this; }

        public ProfileResultsResponse build() {
            return new ProfileResultsResponse(colorTypeResults, bodyShapeResults);
        }
    }

    public static ProfileResultsResponseBuilder builder() {
        return new ProfileResultsResponseBuilder();
    }
}
