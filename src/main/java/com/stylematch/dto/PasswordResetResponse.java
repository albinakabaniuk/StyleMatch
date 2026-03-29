package com.stylematch.dto;

public class PasswordResetResponse {
    private String message;

    public PasswordResetResponse() {}

    public PasswordResetResponse(String message) {
        this.message = message;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public static PasswordResetResponseBuilder builder() {
        return new PasswordResetResponseBuilder();
    }

    public static class PasswordResetResponseBuilder {
        private String message;

        public PasswordResetResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public PasswordResetResponse build() {
            return new PasswordResetResponse(message);
        }
    }
}
