package com.stylematch.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request payload for changing user password")
public class ChangePasswordRequest {

    @Schema(description = "The current plain-text password", example = "oldPassword123")
    @NotBlank(message = "Current password must not be blank")
    private String currentPassword;

    @Schema(description = "The new plain-text password", example = "newStrongPassword456!")
    @NotBlank(message = "New password must not be blank")
    private String newPassword;

    public String getCurrentPassword() { return currentPassword; }
    public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }

    public ChangePasswordRequest() {}
    public ChangePasswordRequest(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }


    public static class ChangePasswordRequestBuilder {
        private String currentPassword;
        private String newPassword;
        public ChangePasswordRequestBuilder currentPassword(String currentPassword) { this.currentPassword = currentPassword; return this; }
        public ChangePasswordRequestBuilder newPassword(String newPassword) { this.newPassword = newPassword; return this; }
        public ChangePasswordRequest build() { return new ChangePasswordRequest(currentPassword, newPassword); }
    }

    public static ChangePasswordRequestBuilder builder() {
        return new ChangePasswordRequestBuilder();
    }
}
