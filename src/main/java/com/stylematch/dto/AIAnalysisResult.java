package com.stylematch.dto;

import java.util.List;

public class AIAnalysisResult {
    private String resultType;
    private String season;
    private String undertone;
    private String contrastLevel;
    private String depth;
    private String chroma;
    private String bodyShape;
    private String summary;
    private List<String> palette;
    private List<String> recommendations;
    private String personalizedAdvice;
    private String imagePrompt;
    private String rawData;

    public AIAnalysisResult() {}

    public AIAnalysisResult(String resultType, String season, String undertone, String contrastLevel, 
                            String depth, String chroma, String bodyShape, 
                            String summary, List<String> palette, List<String> recommendations, 
                            String personalizedAdvice, String imagePrompt, String rawData) {
        this.resultType = resultType;
        this.season = season;
        this.undertone = undertone;
        this.contrastLevel = contrastLevel;
        this.depth = depth;
        this.chroma = chroma;
        this.bodyShape = bodyShape;
        this.summary = summary;
        this.palette = palette;
        this.recommendations = recommendations;
        this.personalizedAdvice = personalizedAdvice;
        this.imagePrompt = imagePrompt;
        this.rawData = rawData;
    }

    public static AIAnalysisResultBuilder builder() {
        return new AIAnalysisResultBuilder();
    }

    public static class AIAnalysisResultBuilder {
        private String resultType;
        private String season;
        private String undertone;
        private String contrastLevel;
        private String depth;
        private String chroma;
        private String bodyShape;
        private String summary;
        private List<String> palette;
        private List<String> recommendations;
        private String personalizedAdvice;
        private String imagePrompt;
        private String rawData;

        public AIAnalysisResultBuilder resultType(String resultType) { this.resultType = resultType; return this; }
        public AIAnalysisResultBuilder season(String season) { this.season = season; return this; }
        public AIAnalysisResultBuilder undertone(String undertone) { this.undertone = undertone; return this; }
        public AIAnalysisResultBuilder contrastLevel(String contrastLevel) { this.contrastLevel = contrastLevel; return this; }
        public AIAnalysisResultBuilder depth(String depth) { this.depth = depth; return this; }
        public AIAnalysisResultBuilder chroma(String chroma) { this.chroma = chroma; return this; }
        public AIAnalysisResultBuilder bodyShape(String bodyShape) { this.bodyShape = bodyShape; return this; }
        public AIAnalysisResultBuilder summary(String summary) { this.summary = summary; return this; }
        public AIAnalysisResultBuilder palette(List<String> palette) { this.palette = palette; return this; }
        public AIAnalysisResultBuilder recommendations(List<String> recommendations) { this.recommendations = recommendations; return this; }
        public AIAnalysisResultBuilder personalizedAdvice(String personalizedAdvice) { this.personalizedAdvice = personalizedAdvice; return this; }
        public AIAnalysisResultBuilder imagePrompt(String imagePrompt) { this.imagePrompt = imagePrompt; return this; }
        public AIAnalysisResultBuilder rawData(String rawData) { this.rawData = rawData; return this; }

        public AIAnalysisResult build() {
            return new AIAnalysisResult(resultType, season, undertone, contrastLevel, depth, chroma, bodyShape, summary, palette, recommendations, personalizedAdvice, imagePrompt, rawData);
        }
    }

    public String getSeason() { return season; }
    public void setSeason(String season) { this.season = season; }

    public String getResultType() { return resultType; }
    public void setResultType(String resultType) { this.resultType = resultType; }
    public String getUndertone() { return undertone; }
    public void setUndertone(String undertone) { this.undertone = undertone; }
    public String getContrastLevel() { return contrastLevel; }
    public void setContrastLevel(String contrastLevel) { this.contrastLevel = contrastLevel; }
    public String getDepth() { return depth; }
    public void setDepth(String depth) { this.depth = depth; }
    public String getChroma() { return chroma; }
    public void setChroma(String chroma) { this.chroma = chroma; }
    public String getBodyShape() { return bodyShape; }
    public void setBodyShape(String bodyShape) { this.bodyShape = bodyShape; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public List<String> getPalette() { return palette; }
    public void setPalette(List<String> palette) { this.palette = palette; }
    public List<String> getRecommendations() { return recommendations; }
    public void setRecommendations(List<String> recommendations) { this.recommendations = recommendations; }
    public String getPersonalizedAdvice() { return personalizedAdvice; }
    public void setPersonalizedAdvice(String personalizedAdvice) { this.personalizedAdvice = personalizedAdvice; }
    public String getImagePrompt() { return imagePrompt; }
    public void setImagePrompt(String imagePrompt) { this.imagePrompt = imagePrompt; }
    public String getRawData() { return rawData; }
    public void setRawData(String rawData) { this.rawData = rawData; }
}
