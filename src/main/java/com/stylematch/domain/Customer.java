package com.stylematch.domain;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "customers")
public class Customer {
    public Customer() {}
    public Customer(UUID id, String name, String email, ColorType colorType, BodyShape bodyShape, ContrastLevel contrastLevel, Undertone undertone, java.time.LocalDateTime createdAt, java.time.LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.colorType = colorType;
        this.bodyShape = bodyShape;
        this.contrastLevel = contrastLevel;
        this.undertone = undertone;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "color_type")
    private ColorType colorType;

    @Enumerated(EnumType.STRING)
    @Column(name = "body_shape")
    private BodyShape bodyShape;

    @Enumerated(EnumType.STRING)
    @Column(name = "contrast_level")
    private ContrastLevel contrastLevel;

    @Enumerated(EnumType.STRING)
    private Undertone undertone;

    @org.hibernate.annotations.CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private java.time.LocalDateTime createdAt;

    @org.hibernate.annotations.UpdateTimestamp
    private java.time.LocalDateTime updatedAt;

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
    public java.time.LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(java.time.LocalDateTime createdAt) { this.createdAt = createdAt; }
    public java.time.LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(java.time.LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static class CustomerBuilder {
        private UUID id;
        private String name;
        private String email;
        private ColorType colorType;
        private BodyShape bodyShape;
        private ContrastLevel contrastLevel;
        private Undertone undertone;
        private java.time.LocalDateTime createdAt;
        private java.time.LocalDateTime updatedAt;

        public CustomerBuilder id(UUID id) { this.id = id; return this; }
        public CustomerBuilder name(String name) { this.name = name; return this; }
        public CustomerBuilder email(String email) { this.email = email; return this; }
        public CustomerBuilder colorType(ColorType colorType) { this.colorType = colorType; return this; }
        public CustomerBuilder bodyShape(BodyShape bodyShape) { this.bodyShape = bodyShape; return this; }
        public CustomerBuilder contrastLevel(ContrastLevel contrastLevel) { this.contrastLevel = contrastLevel; return this; }
        public CustomerBuilder undertone(Undertone undertone) { this.undertone = undertone; return this; }
        public CustomerBuilder createdAt(java.time.LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public CustomerBuilder updatedAt(java.time.LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Customer build() {
            return new Customer(id, name, email, colorType, bodyShape, contrastLevel, undertone, createdAt, updatedAt);
        }
    }

    public static CustomerBuilder builder() {
        return new CustomerBuilder();
    }
}
