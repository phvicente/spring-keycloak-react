package com.wogoo.backend.mappers;

import com.wogoo.backend.controller.dto.LoanResponse;
import com.wogoo.backend.model.Loan;
import org.springframework.stereotype.Component;

@Component
public class LoanMapper {

    public LoanResponse toLoanResponse(Loan loan) {
        LoanResponse loanResponse = new LoanResponse();

        loanResponse.setLoanId(loan.getLoanId());
        loanResponse.setLoanValue(loan.getLoanValue());
        loanResponse.setTaxLoan(loan.getTaxLoan());
        loanResponse.setTotalLoanValue(loan.getTotalLoanValue());
        loanResponse.setNumberInstallments(loan.getNumberInstallments());
        loanResponse.setStatus(loan.getStatus());

        return loanResponse;
    }



}
