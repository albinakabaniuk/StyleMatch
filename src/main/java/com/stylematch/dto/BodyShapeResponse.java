package com.stylematch.dto;

import com.stylematch.domain.BodyShape;
import java.util.List;

public class BodyShapeResponse {
    private BodyShape bodyShapeType;
    private String bodyShapeDisplayName;
    private String explanation;
    private List<String> recommendations;
    private AIAnalysisResult aiResult;
    private String personalizedAdvice;
    private List<String> palette;
    private String season;

    public BodyShapeResponse() {}

    public BodyShapeResponse(BodyShape bodyShapeType, String bodyShapeDisplayName, String explanation, 
                            List<String> recommendations, AIAnalysisResult aiResult, String personalizedAdvice,
                            List<String> palette, String season) {
        this.bodyShapeType = bodyShapeType;
        this.bodyShapeDisplayName = bodyShapeDisplayName;
        this.explanation = explanation;
        this.recommendations = recommendations;
        this.aiResult = aiResult;
        this.personalizedAdvice = personalizedAdvice;
        this.palette = palette;
        this.season = season;
    }

    public BodyShape getBodyShapeType() { return bodyShapeType; }
    public void setBodyShapeType(BodyShape bodyShapeType) { this.bodyShapeType = bodyShapeType; }
    public String getBodyShapeDisplayName() { return bodyShapeDisplayName; }
    public void setBodyShapeDisplayName(String bodyShapeDisplayName) { this.bodyShapeDisplayName = bodyShapeDisplayName; }
    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }
    public List<String> getRecommendations() { return recommendations; }
    public void setRecommendations(List<String> recommendations) { this.recommendations = recommendations; }
    public AIAnalysisResult getAiResult() { return aiResult; }
    public void setAiResult(AIAnalysisResult aiResult) { this.aiResult = aiResult; }
    public String getPersonalizedAdvice() { return personalizedAdvice; }
    public void setPersonalizedAdvice(String personalizedAdvice) { this.personalizedAdvice = personalizedAdvice; }
    public List<String> getPalette() { return palette; }
    public void setPalette(List<String> palette) { this.palette = palette; }

    public static class BodyShapeResponseBuilder {
        private BodyShape bodyShapeType;
        private String bodyShapeDisplayName;
        private String explanation;
        private List<String> recommendations;
        private AIAnalysisResult aiResult;
        private String personalizedAdvice;
        private List<String> palette;
        private String season;

        public BodyShapeResponseBuilder bodyShapeType(BodyShape bodyShapeType) { this.bodyShapeType = bodyShapeType; return this; }
        public BodyShapeResponseBuilder bodyShapeDisplayName(String bodyShapeDisplayName) { this.bodyShapeDisplayName = bodyShapeDisplayName; return this; }
        public BodyShapeResponseBuilder explanation(String explanation) { this.explanation = explanation; return this; }
        public BodyShapeResponseBuilder recommendations(List<String> recommendations) { this.recommendations = recommendations; return this; }
        public BodyShapeResponseBuilder aiResult(AIAnalysisResult aiResult) { this.aiResult = aiResult; return this; }
        public BodyShapeResponseBuilder personalizedAdvice(String personalizedAdvice) { this.personalizedAdvice = personalizedAdvice; return this; }
        public BodyShapeResponseBuilder palette(List<String> palette) { this.palette = palette; return this; }
        public BodyShapeResponseBuilder season(String season) { this.season = season; return this; }

        public BodyShapeResponse build() {
            return new BodyShapeResponse(bodyShapeType, bodyShapeDisplayName, explanation, recommendations, aiResult, personalizedAdvice, palette, season);
        }
    }

    public String getSeason() { return season; }
    public void setSeason(String season) { this.season = season; }

    public static BodyShapeResponseBuilder builder() {
        return new BodyShapeResponseBuilder();
    }
}
