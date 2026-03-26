package com.stylematch.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ResetPasswordRequest {

    @NotBlank
    private String token;

    @NotBlank
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String newPassword;

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }

    public ResetPasswordRequest() {}
    public ResetPasswordRequest(String token, String newPassword) {
        this.token = token;
        this.newPassword = newPassword;
    }

    public static class ResetPasswordRequestBuilder {
        private String token;
        private String newPassword;
        public ResetPasswordRequestBuilder token(String token) { this.token = token; return this; }
        public ResetPasswordRequestBuilder newPassword(String newPassword) { this.newPassword = newPassword; return this; }
        public ResetPasswordRequest build() { return new ResetPasswordRequest(token, newPassword); }
    }

    public static ResetPasswordRequestBuilder builder() {
        return new ResetPasswordRequestBuilder();
    }
}
