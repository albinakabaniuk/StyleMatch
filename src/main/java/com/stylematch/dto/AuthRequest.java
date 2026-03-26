package com.stylematch.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
public class AuthRequest {
    public AuthRequest() {}
    public AuthRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
    @Schema(example = "user@example.com")
    @NotBlank
    @Email
    private String email;

    @Schema(example = "password123")
    @NotBlank
    private String password;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    // Manual builder
    public static class AuthRequestBuilder {
        private String email;
        private String password;

        public AuthRequestBuilder email(String email) { this.email = email; return this; }
        public AuthRequestBuilder password(String password) { this.password = password; return this; }

        public AuthRequest build() {
            return new AuthRequest(email, password);
        }
    }

    public static AuthRequestBuilder builder() {
        return new AuthRequestBuilder();
    }
}
