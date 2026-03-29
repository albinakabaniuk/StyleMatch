package com.stylematch.dto;

public class PasswordResetResponse {
    private String message;
    private String token;

    public PasswordResetResponse() {}

    public PasswordResetResponse(String message, String token) {
        this.message = message;
        this.token = token;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public static PasswordResetResponseBuilder builder() {
        return new PasswordResetResponseBuilder();
    }

    public static class PasswordResetResponseBuilder {
        private String message;
        private String token;

        public PasswordResetResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public PasswordResetResponseBuilder token(String token) {
            this.token = token;
            return this;
        }

        public PasswordResetResponse build() {
            return new PasswordResetResponse(message, token);
        }
    }
}
