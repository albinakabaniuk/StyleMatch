package com.stylematch.domain;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Skin undertone")
public enum Undertone {
    WARM,
    COOL,
    NEUTRAL
}
