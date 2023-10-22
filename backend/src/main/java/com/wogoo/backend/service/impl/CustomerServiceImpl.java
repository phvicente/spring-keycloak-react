package com.wogoo.backend.service.impl;

import com.wogoo.backend.model.Customer;
import com.wogoo.backend.repository.CustomerRepository;
import com.wogoo.backend.service.CustomerService;
import jakarta.ws.rs.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public Optional<Customer> findById(UUID id) {
        return Optional.ofNullable(customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Customer with id '%s' not found", id)
                )));
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }
}
