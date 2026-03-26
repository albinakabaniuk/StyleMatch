package com.stylematch.dto;

import com.stylematch.domain.BodyShape;
import com.stylematch.domain.ColorType;
import com.stylematch.domain.ContrastLevel;
import com.stylematch.domain.Undertone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
@Schema(description = "Request payload to update an existing customer")
public class UpdateCustomerRequest {

    @Schema(description = "Full name of the customer", example = "Chloe Yazzie")
    @NotBlank(message = "Name is required")
    private String name;

    @Schema(description = "Email address for the customer", example = "chloe@example.com")
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @Schema(description = "Determined seasonal color type", example = "WINTER")
    private ColorType colorType;

    @Schema(description = "Kibbe body shape category", example = "X")
    private BodyShape bodyShape;

    @Schema(description = "Contrast level of features", example = "HIGH")
    private ContrastLevel contrastLevel;

    @Schema(description = "Skin undertone", example = "COOL")
    private Undertone undertone;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public ColorType getColorType() { return colorType; }
    public void setColorType(ColorType colorType) { this.colorType = colorType; }
    public BodyShape getBodyShape() { return bodyShape; }
    public void setBodyShape(BodyShape bodyShape) { this.bodyShape = bodyShape; }
    public ContrastLevel getContrastLevel() { return contrastLevel; }
    public void setContrastLevel(ContrastLevel contrastLevel) { this.contrastLevel = contrastLevel; }
    public Undertone getUndertone() { return undertone; }
    public void setUndertone(Undertone undertone) { this.undertone = undertone; }

    public UpdateCustomerRequest() {}
    public UpdateCustomerRequest(String name, String email, ColorType colorType, 
                                BodyShape bodyShape, ContrastLevel contrastLevel, Undertone undertone) {
        this.name = name;
        this.email = email;
        this.colorType = colorType;
        this.bodyShape = bodyShape;
        this.contrastLevel = contrastLevel;
        this.undertone = undertone;
    }

    public static class UpdateCustomerRequestBuilder {
        private String name;
        private String email;
        private ColorType colorType;
        private BodyShape bodyShape;
        private ContrastLevel contrastLevel;
        private Undertone undertone;

        public UpdateCustomerRequestBuilder name(String name) { this.name = name; return this; }
        public UpdateCustomerRequestBuilder email(String email) { this.email = email; return this; }
        public UpdateCustomerRequestBuilder colorType(ColorType colorType) { this.colorType = colorType; return this; }
        public UpdateCustomerRequestBuilder bodyShape(BodyShape bodyShape) { this.bodyShape = bodyShape; return this; }
        public UpdateCustomerRequestBuilder contrastLevel(ContrastLevel contrastLevel) { this.contrastLevel = contrastLevel; return this; }
        public UpdateCustomerRequestBuilder undertone(Undertone undertone) { this.undertone = undertone; return this; }

        public UpdateCustomerRequest build() {
            return new UpdateCustomerRequest(name, email, colorType, bodyShape, contrastLevel, undertone);
        }
    }

    public static UpdateCustomerRequestBuilder builder() {
        return new UpdateCustomerRequestBuilder();
    }
}
