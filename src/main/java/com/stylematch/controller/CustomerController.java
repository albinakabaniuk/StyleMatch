package com.stylematch.controller;

import com.stylematch.dto.CreateCustomerRequest;
import com.stylematch.dto.CustomerResponse;
import com.stylematch.dto.UpdateCustomerRequest;
import com.stylematch.service.CustomerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customers", description = "Operations related to StyleMatch customers")
public class CustomerController {

    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Create a new customer", description = "Creates a new customer profile with color type and body shape details")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Customer created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload")
    })
    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(
            @Parameter(description = "Customer creation request body") @Valid @RequestBody CreateCustomerRequest request) {
        log.info("REST request to create customer: {}", request.getEmail());
        CustomerResponse response = customerService.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get a customer by ID", description = "Retrieves a customer by their unique identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer found"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomer(
            @Parameter(description = "UUID of the customer", example = "123e4567-e89b-12d3-a456-426614174000") @PathVariable("id") UUID id) {
        log.info("REST request to get customer: {}", id);
        CustomerResponse response = customerService.getCustomerById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all customers", description = "Retrieves a list of all custom profiles")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of customers")
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        log.info("REST request to get all customers");
        List<CustomerResponse> responses = customerService.getAllCustomers();
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Update a customer", description = "Updates an existing customer profile")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @Parameter(description = "UUID of the customer", example = "123e4567-e89b-12d3-a456-426614174000") @PathVariable("id") UUID id,
            @Parameter(description = "Customer update request body") @Valid @RequestBody UpdateCustomerRequest request) {
        log.info("REST request to update customer: {}", id);
        CustomerResponse response = customerService.updateCustomer(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a customer", description = "Deletes a customer profile from the system")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(
            @Parameter(description = "UUID of the customer", example = "123e4567-e89b-12d3-a456-426614174000") @PathVariable("id") UUID id) {
        log.info("REST request to delete customer: {}", id);
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
