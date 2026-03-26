package com.stylematch.dto;

import com.stylematch.domain.BodyShape;
import com.stylematch.domain.ColorType;
import com.stylematch.domain.ContrastLevel;
import com.stylematch.domain.Undertone;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
@Schema(description = "Request payload to create a new customer")
public class CreateCustomerRequest {

    @Schema(description = "Full name of the customer", example = "Chloe Yazzie")
    @NotBlank(message = "Name is required")
    private String name;

    @Schema(description = "Email address for the customer", example = "chloe@example.com")
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @Schema(description = "Determined seasonal color type", example = "WINTER")
    @NotNull(message = "Color type is required")
    private ColorType colorType;

    @Schema(description = "Kibbe body shape category", example = "X")
    @NotNull(message = "Body shape is required")
    private BodyShape bodyShape;

    @Schema(description = "Contrast level of features", example = "HIGH")
    @NotNull(message = "Contrast level is required")
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

    public CreateCustomerRequest() {}
    public CreateCustomerRequest(String name, String email, ColorType colorType, 
                                BodyShape bodyShape, ContrastLevel contrastLevel, Undertone undertone) {
        this.name = name;
        this.email = email;
        this.colorType = colorType;
        this.bodyShape = bodyShape;
        this.contrastLevel = contrastLevel;
        this.undertone = undertone;
    }

    public static class CreateCustomerRequestBuilder {
        private String name;
        private String email;
        private ColorType colorType;
        private BodyShape bodyShape;
        private ContrastLevel contrastLevel;
        private Undertone undertone;

        public CreateCustomerRequestBuilder name(String name) { this.name = name; return this; }
        public CreateCustomerRequestBuilder email(String email) { this.email = email; return this; }
        public CreateCustomerRequestBuilder colorType(ColorType colorType) { this.colorType = colorType; return this; }
        public CreateCustomerRequestBuilder bodyShape(BodyShape bodyShape) { this.bodyShape = bodyShape; return this; }
        public CreateCustomerRequestBuilder contrastLevel(ContrastLevel contrastLevel) { this.contrastLevel = contrastLevel; return this; }
        public CreateCustomerRequestBuilder undertone(Undertone undertone) { this.undertone = undertone; return this; }

        public CreateCustomerRequest build() {
            return new CreateCustomerRequest(name, email, colorType, bodyShape, contrastLevel, undertone);
        }
    }

    public static CreateCustomerRequestBuilder builder() {
        return new CreateCustomerRequestBuilder();
    }
}
