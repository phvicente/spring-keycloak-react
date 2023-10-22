package com.wogoo.backend.controller.dto;

import com.wogoo.backend.enums.LoanStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class LoanResponse {

    @NotBlank
    private UUID loanId;

    @NotBlank
    private Double loanValue;

    @NotBlank
    private Integer taxLoan;

    @NotBlank
    private Double totalLoanValue;

    @NotBlank
    private Integer numberInstallments;

    @NotBlank
    private LoanStatus status;

}
