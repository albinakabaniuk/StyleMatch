package com.stylematch.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
public class RegisterRequest {
    public RegisterRequest() {}
    public RegisterRequest(String email, String password) {
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
    public static class RegisterRequestBuilder {
        private String email;
        private String password;

        public RegisterRequestBuilder email(String email) { this.email = email; return this; }
        public RegisterRequestBuilder password(String password) { this.password = password; return this; }

        public RegisterRequest build() {
            return new RegisterRequest(email, password);
        }
    }

    public static RegisterRequestBuilder builder() {
        return new RegisterRequestBuilder();
    }
}
