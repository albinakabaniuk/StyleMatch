package com.stylematch.dto;

import com.stylematch.domain.BodyShape;
import com.stylematch.domain.ColorType;
import com.stylematch.domain.ContrastLevel;
import com.stylematch.domain.Undertone;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
@Schema(description = "Response object representing a StyleMatch customer")
public class CustomerResponse {
    @Schema(description = "Unique identifier of the customer", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Full name of the customer", example = "Chloe Yazzie")
    private String name;

    @Schema(description = "Email address", example = "chloe@example.com")
    private String email;

    @Schema(description = "Determined seasonal color type", example = "WINTER")
    private ColorType colorType;

    @Schema(description = "Kibbe body shape category", example = "X")
    private BodyShape bodyShape;

    @Schema(description = "Contrast level of features", example = "HIGH")
    private ContrastLevel contrastLevel;

    @Schema(description = "Skin undertone", example = "COOL")
    private Undertone undertone;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
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

    public CustomerResponse() {}
    public CustomerResponse(UUID id, String name, String email, ColorType colorType, 
                           BodyShape bodyShape, ContrastLevel contrastLevel, Undertone undertone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.colorType = colorType;
        this.bodyShape = bodyShape;
        this.contrastLevel = contrastLevel;
        this.undertone = undertone;
    }

    public static class CustomerResponseBuilder {
        private UUID id;
        private String name;
        private String email;
        private ColorType colorType;
        private BodyShape bodyShape;
        private ContrastLevel contrastLevel;
        private Undertone undertone;

        public CustomerResponseBuilder id(UUID id) { this.id = id; return this; }
        public CustomerResponseBuilder name(String name) { this.name = name; return this; }
        public CustomerResponseBuilder email(String email) { this.email = email; return this; }
        public CustomerResponseBuilder colorType(ColorType colorType) { this.colorType = colorType; return this; }
        public CustomerResponseBuilder bodyShape(BodyShape bodyShape) { this.bodyShape = bodyShape; return this; }
        public CustomerResponseBuilder contrastLevel(ContrastLevel contrastLevel) { this.contrastLevel = contrastLevel; return this; }
        public CustomerResponseBuilder undertone(Undertone undertone) { this.undertone = undertone; return this; }

        public CustomerResponse build() {
            return new CustomerResponse(id, name, email, colorType, bodyShape, contrastLevel, undertone);
        }
    }

    public static CustomerResponseBuilder builder() {
        return new CustomerResponseBuilder();
    }
}
