package com.stylematch.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ForgotPasswordRequest {

    @Email
    @NotBlank
    private String email;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public ForgotPasswordRequest() {}
    public ForgotPasswordRequest(String email) { this.email = email; }

    public static class ForgotPasswordRequestBuilder {
        private String email;
        public ForgotPasswordRequestBuilder email(String email) { this.email = email; return this; }
        public ForgotPasswordRequest build() { return new ForgotPasswordRequest(email); }
    }

    public static ForgotPasswordRequestBuilder builder() {
        return new ForgotPasswordRequestBuilder();
    }
}
