package com.stylematch.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Health", description = "API Health Check endpoints")
public class HealthController {

    @Operation(summary = "Health check endpoint", description = "Returns 'pong' to verify the application is running.")
    @GetMapping({"/ping", "/api/health/ping"})
    public String ping() {
        return "pong";
    }
}
