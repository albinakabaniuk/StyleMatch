package com.stylematch.domain;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Body shape categories")
public enum BodyShape {
    HOURGLASS("Hourglass", "Balanced shoulders and hips with a defined waist."),
    RECTANGLE("Rectangle", "Shoulders, bust, and hips are fairly uniform with little waist definition."),
    PEAR("Pear", "Hips are wider than the shoulders and bust."),
    INVERTED_TRIANGLE("Inverted Triangle", "Shoulders or bust are wider than the hips."),
    APPLE("Apple", "Bust and waist are wider than the hips.");

    private final String displayName;
    private final String description;

    BodyShape(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
