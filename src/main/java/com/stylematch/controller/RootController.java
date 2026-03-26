package com.stylematch.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Root", description = "Root diagnostic endpoints")
public class RootController {

    @Operation(summary = "Root diagnostic endpoint", description = "Identifies the backend service.")
    @GetMapping("/")
    public String root() {
        return "StyleMatch Backend API is Running. Please visit the Frontend application URL to use the app.";
    }
}
