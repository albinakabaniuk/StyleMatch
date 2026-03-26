package com.stylematch.dto;

import io.swagger.v3.oas.annotations.media.Schema;
public class AuthResponse {
    public AuthResponse() {}
    public AuthResponse(String token) {
        this.token = token;
    }
    @Schema(description = "JWT token for authorization")
    private String token;

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    // Manual builder
    public static class AuthResponseBuilder {
        private String token;
        public AuthResponseBuilder token(String token) { this.token = token; return this; }
        public AuthResponse build() { return new AuthResponse(token); }
    }

    public static AuthResponseBuilder builder() {
        return new AuthResponseBuilder();
    }
}
