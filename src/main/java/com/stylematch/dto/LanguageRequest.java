package com.stylematch.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request payload for updating preferred language")
public class LanguageRequest {

    @Schema(description = "The new preferred language code", example = "uk")
    @NotBlank(message = "Language must not be blank")
    private String language;

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public LanguageRequest() {}
    public LanguageRequest(String language) { this.language = language; }

    public static class LanguageRequestBuilder {
        private String language;
        public LanguageRequestBuilder language(String language) { this.language = language; return this; }
        public LanguageRequest build() { return new LanguageRequest(language); }
    }

    public static LanguageRequestBuilder builder() { return new LanguageRequestBuilder(); }
}
