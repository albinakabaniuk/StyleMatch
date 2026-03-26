package com.stylematch.dto;
import java.util.List;

import com.stylematch.domain.BodyShape;
import com.stylematch.domain.ColorType;
import com.stylematch.domain.ContrastLevel;
import com.stylematch.domain.Undertone;
import io.swagger.v3.oas.annotations.media.Schema;
@Schema(description = "Response payload containing the results of the style analysis")
public class AnalysisResponse {

    @Schema(description = "Determined seasonal color type", example = "WINTER")
    private ColorType colorType;

    @Schema(description = "Skin undertone", example = "COOL")
    private Undertone undertone;

    @Schema(description = "Contrast level of features", example = "HIGH")
    private ContrastLevel contrastLevel;

    @Schema(description = "Color depth", example = "DEEP")
    private String depth;

    @Schema(description = "Color chroma", example = "CLEAR")
    private String chroma;

    @Schema(description = "Kibbe body shape category (Test-only; photo mock doesn't support body shape yet)", example = "X")
    private BodyShape bodyShape;

    @Schema(description = "Seasonal color category", example = "WINTER")
    private String season;

    @Schema(description = "A short explanatory message about the results", example = "Analysis completed using rule-based scoring.")
    private String message;

    @Schema(description = "Dynamic color palette for the seasonal type")
    private List<String> palette;

    @Schema(description = "Detailed advice personalized to user metrics (age, height, weight)")
    private String personalizedAdvice;

    @Schema(description = "Detailed AI-generated analysis result")
    private AIAnalysisResult aiResult;

    public AnalysisResponse() {}

    public AnalysisResponse(ColorType colorType, Undertone undertone, ContrastLevel contrastLevel, 
                            String depth, String chroma,
                            BodyShape bodyShape, String season, String message, List<String> palette, 
                            String personalizedAdvice, AIAnalysisResult aiResult) {
        this.colorType = colorType;
        this.undertone = undertone;
        this.contrastLevel = contrastLevel;
        this.depth = depth;
        this.chroma = chroma;
        this.bodyShape = bodyShape;
        this.season = season;
        this.message = message;
        this.palette = palette;
        this.personalizedAdvice = personalizedAdvice;
        this.aiResult = aiResult;
    }

    public static AnalysisResponseBuilder builder() {
        return new AnalysisResponseBuilder();
    }

    public static class AnalysisResponseBuilder {
        private ColorType colorType;
        private Undertone undertone;
        private ContrastLevel contrastLevel;
        private String depth;
        private String chroma;
        private BodyShape bodyShape;
        private String season;
        private String message;
        private List<String> palette;
        private String personalizedAdvice;
        private AIAnalysisResult aiResult;

        public AnalysisResponseBuilder colorType(ColorType colorType) { this.colorType = colorType; return this; }
        public AnalysisResponseBuilder undertone(Undertone undertone) { this.undertone = undertone; return this; }
        public AnalysisResponseBuilder contrastLevel(ContrastLevel contrastLevel) { this.contrastLevel = contrastLevel; return this; }
        public AnalysisResponseBuilder depth(String depth) { this.depth = depth; return this; }
        public AnalysisResponseBuilder chroma(String chroma) { this.chroma = chroma; return this; }
        public AnalysisResponseBuilder bodyShape(BodyShape bodyShape) { this.bodyShape = bodyShape; return this; }
        public AnalysisResponseBuilder season(String season) { this.season = season; return this; }
        public AnalysisResponseBuilder message(String message) { this.message = message; return this; }
        public AnalysisResponseBuilder palette(List<String> palette) { this.palette = palette; return this; }
        public AnalysisResponseBuilder personalizedAdvice(String personalizedAdvice) { this.personalizedAdvice = personalizedAdvice; return this; }
        public AnalysisResponseBuilder aiResult(AIAnalysisResult aiResult) { this.aiResult = aiResult; return this; }

        public AnalysisResponse build() {
            return new AnalysisResponse(colorType, undertone, contrastLevel, depth, chroma, bodyShape, season, message, palette, personalizedAdvice, aiResult);
        }
    }

    public ColorType getColorType() { return colorType; }
    public void setColorType(ColorType colorType) { this.colorType = colorType; }
    public Undertone getUndertone() { return undertone; }
    public void setUndertone(Undertone undertone) { this.undertone = undertone; }
    public ContrastLevel getContrastLevel() { return contrastLevel; }
    public void setContrastLevel(ContrastLevel contrastLevel) { this.contrastLevel = contrastLevel; }
    public String getDepth() { return depth; }
    public void setDepth(String depth) { this.depth = depth; }
    public String getChroma() { return chroma; }
    public void setChroma(String chroma) { this.chroma = chroma; }
    public BodyShape getBodyShape() { return bodyShape; }
    public void setBodyShape(BodyShape bodyShape) { this.bodyShape = bodyShape; }
    public String getSeason() { return season; }
    public void setSeason(String season) { this.season = season; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public List<String> getPalette() { return palette; }
    public void setPalette(List<String> palette) { this.palette = palette; }
    public String getPersonalizedAdvice() { return personalizedAdvice; }
    public void setPersonalizedAdvice(String personalizedAdvice) { this.personalizedAdvice = personalizedAdvice; }
    public AIAnalysisResult getAiResult() { return aiResult; }
    public void setAiResult(AIAnalysisResult aiResult) { this.aiResult = aiResult; }
}
