package com.wogoo.backend.service;

import com.wogoo.backend.controller.dto.CreateLoanRequest;
import com.wogoo.backend.controller.dto.LoanResponse;
import com.wogoo.backend.model.Loan;

import java.util.List;
import java.util.UUID;


public interface LoanService {
    Loan createLoan(CreateLoanRequest loan, String token);

    List<LoanResponse> findAllByCustomer(String authorizationHeader);

    LoanResponse findByLoanId(UUID customerId);

}
