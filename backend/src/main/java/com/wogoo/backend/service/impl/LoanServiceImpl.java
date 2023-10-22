package com.wogoo.backend.service.impl;

import com.wogoo.backend.controller.dto.CreateLoanRequest;
import com.wogoo.backend.controller.dto.LoanResponse;
import com.wogoo.backend.enums.LoanStatus;
import com.wogoo.backend.mappers.LoanMapper;
import com.wogoo.backend.model.Customer;
import com.wogoo.backend.model.Loan;
import com.wogoo.backend.repository.CustomerRepository;
import com.wogoo.backend.repository.LoanRepository;
import com.wogoo.backend.security.JwtAuthConverter;
import com.wogoo.backend.service.LoanService;
import jakarta.ws.rs.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final CustomerServiceImpl customerService;
    private final JwtAuthConverter jwtAuthConverter;
    private final CustomerRepository customerRepository;
    private final LoanMapper loanMapper;

    private final Integer TAX_LOAN = 25;

    @Override
    public Loan createLoan(CreateLoanRequest loanRequest, String token) {

        UUID keycloakUserId = jwtAuthConverter.convertTokenToUserInfo(token);

        Customer customer = customerRepository.findByKeycloakUserId(keycloakUserId)
                .orElseGet( () -> {
                    Customer newCustomer = new Customer();
                    newCustomer.setKeycloakUserId(keycloakUserId);
                    return customerRepository.save(newCustomer);
                });

        Loan newLoan = new Loan();
        newLoan.setLoanValue(loanRequest.getLoanValue());
        newLoan.setCustomer(customer);
        newLoan.setTaxLoan(TAX_LOAN);
        double montlyTax = TAX_LOAN / 12.0 / 100.0;
        newLoan.setTotalLoanValue(loanRequest.getLoanValue() * (1 + montlyTax * loanRequest.getNumberInstallments()));
        newLoan.setNumberInstallments(loanRequest.getNumberInstallments());
        newLoan.setStatus(LoanStatus.PENDING);

        return loanRepository.save(newLoan);
    }

    @Override
    public List<LoanResponse> findAllByCustomer(String authorizationHeader) {

        UUID keycloakUserId = jwtAuthConverter.convertTokenToUserInfo(authorizationHeader);

        Customer customer = customerRepository.findByKeycloakUserId(keycloakUserId)
                .orElseThrow(
                        () -> new NotFoundException("Customer not found")
                );
        List<Loan> loans = loanRepository.findAllByCustomer(customer);
        return loans.stream()
                .map(loanMapper::toLoanResponse)
                .collect(Collectors.toList());
    }


    @Override
    public LoanResponse findByLoanId(UUID id) {
        Loan loan = loanRepository.findByLoanId(id).orElseThrow(
                () -> new NotFoundException("Loan not found")
        );
        return loanMapper.toLoanResponse(loan);
    }
}
