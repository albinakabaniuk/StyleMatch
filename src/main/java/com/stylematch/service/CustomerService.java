package com.stylematch.service;

import com.stylematch.dto.CreateCustomerRequest;
import com.stylematch.dto.CustomerResponse;
import com.stylematch.dto.UpdateCustomerRequest;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    CustomerResponse createCustomer(CreateCustomerRequest request);

    CustomerResponse getCustomerById(UUID id);

    List<CustomerResponse> getAllCustomers();

    CustomerResponse updateCustomer(UUID id, UpdateCustomerRequest request);

    void deleteCustomer(UUID id);
}
