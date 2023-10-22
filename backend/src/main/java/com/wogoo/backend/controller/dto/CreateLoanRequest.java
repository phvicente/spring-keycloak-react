package com.wogoo.backend.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateLoanRequest {

    @NotNull
    private Double loanValue;

    @NotNull
    private Integer numberInstallments;


}
