package com.wogoo.backend.controller;

import com.wogoo.backend.controller.dto.CreateLoanRequest;
import com.wogoo.backend.controller.dto.LoanResponse;
import com.wogoo.backend.model.Loan;
import com.wogoo.backend.security.JwtAuthConverter;
import com.wogoo.backend.service.CustomerService;
import com.wogoo.backend.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/loan")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LoanController {

    private final LoanService loanService;


    @PostMapping
    public ResponseEntity<Loan> createLoan(@RequestBody @Valid CreateLoanRequest createLoanRequest,
                                           @RequestHeader("Authorization") String authorizationHeader) {

        Loan loan = loanService.createLoan(createLoanRequest, authorizationHeader);
        return ResponseEntity.status(HttpStatus.CREATED).body(loan);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanResponse> getLoanById(@PathVariable UUID id) {
        LoanResponse loan = loanService.findByLoanId(id);
        return ResponseEntity.ok().body(loan);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Loan>> getLoanByCustomerId(@PathVariable UUID customerId) {
        List<Loan> loans = loanService.findAllByCustomer(customerId);
        return ResponseEntity.ok().body(loans);
    }

}
