package com.wogoo.backend.model;


import com.wogoo.backend.enums.LoanStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Loan")
public class Loan {
    @Id
    @GeneratedValue
    private UUID loanId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @NotNull
    private Double loanValue;

    @NotNull
    private Integer taxLoan;

    @NotNull
    private Double totalLoanValue;

    @NotNull
    private Integer numberInstallments;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
