package com.wogoo.backend.repository;

import com.wogoo.backend.model.Customer;
import com.wogoo.backend.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LoanRepository extends JpaRepository<Loan, UUID> {
    List<Loan> findAllByCustomer(Customer customer);

    Optional<Loan> findByLoanId(UUID loanId);
}