package com.wogoo.backend.service;

import com.wogoo.backend.model.Customer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

    Optional<Customer> findById(UUID id);

    Customer save(Customer customer);

}
