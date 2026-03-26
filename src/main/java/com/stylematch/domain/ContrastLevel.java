package com.stylematch.domain;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Contrast level of features")
public enum ContrastLevel {
    LOW,
    MEDIUM,
    HIGH
}
