package com.stylematch.mapper;

import com.stylematch.domain.Customer;
import com.stylematch.dto.CreateCustomerRequest;
import com.stylematch.dto.CustomerResponse;
import com.stylematch.dto.UpdateCustomerRequest;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer toEntity(CreateCustomerRequest request) {
        return Customer.builder()
                .name(request.getName())
                .email(request.getEmail())
                .colorType(request.getColorType())
                .bodyShape(request.getBodyShape())
                .contrastLevel(request.getContrastLevel())
                .undertone(request.getUndertone())
                .build();
    }

    public void updateEntity(Customer customer, UpdateCustomerRequest request) {
        if (request.getName() != null)
            customer.setName(request.getName());
        if (request.getEmail() != null)
            customer.setEmail(request.getEmail());
        if (request.getColorType() != null)
            customer.setColorType(request.getColorType());
        if (request.getBodyShape() != null)
            customer.setBodyShape(request.getBodyShape());
        if (request.getContrastLevel() != null)
            customer.setContrastLevel(request.getContrastLevel());
        if (request.getUndertone() != null)
            customer.setUndertone(request.getUndertone());
    }

    public CustomerResponse toResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .colorType(customer.getColorType())
                .bodyShape(customer.getBodyShape())
                .contrastLevel(customer.getContrastLevel())
                .undertone(customer.getUndertone())
                .build();
    }
}
