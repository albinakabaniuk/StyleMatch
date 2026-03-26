package com.stylematch.domain;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Determined seasonal color type")
public enum ColorType {
    WINTER,
    SPRING,
    SUMMER,
    AUTUMN,
    
    // Sub-types
    DEEP_WINTER,
    COOL_WINTER,
    BRIGHT_WINTER,
    
    LIGHT_SUMMER,
    COOL_SUMMER,
    SOFT_SUMMER,
    
    LIGHT_SPRING,
    WARM_SPRING,
    BRIGHT_SPRING,
    
    DEEP_AUTUMN,
    WARM_AUTUMN,
    SOFT_AUTUMN
}
