package com.stylematch.config;

import com.stylematch.domain.BodyShape;
import com.stylematch.domain.ColorType;
import com.stylematch.domain.ContrastLevel;
import com.stylematch.domain.Undertone;
import com.stylematch.dto.CreateCustomerRequest;
import com.stylematch.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);
    private final CustomerService customerService;

    public DataInitializer(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            log.info("Checking if sample data needs to be initialized...");
            try {
                if (customerService.getAllCustomers().isEmpty()) {
                    log.info("No customers found. Initializing sample data...");

                    customerService.createCustomer(CreateCustomerRequest.builder()
                            .name("Alice Smith")
                            .email("alice.smith@example.com")
                            .colorType(ColorType.SUMMER)
                            .bodyShape(BodyShape.HOURGLASS)
                            .contrastLevel(ContrastLevel.LOW)
                            .undertone(Undertone.COOL)
                            .build());

                    customerService.createCustomer(CreateCustomerRequest.builder()
                            .name("Bob Jones")
                            .email("bob.jones@example.com")
                            .colorType(ColorType.AUTUMN)
                            .bodyShape(BodyShape.INVERTED_TRIANGLE)
                            .contrastLevel(ContrastLevel.HIGH)
                            .undertone(Undertone.WARM)
                            .build());

                    log.info("Sample data initialized successfully.");
                }
            } catch (Exception e) {
                log.warn("Could not initialize sample data (database might not be ready yet): {}", e.getMessage());
            }
        };
    }
}
