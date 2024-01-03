package com.template.states;

import com.template.contracts.LoanContract;
import java.util.Arrays;
import java.util.List;
import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;

// *********
// * State *
// *********
@BelongsToContract(LoanContract.class)
public class LoanState implements ContractState {

    private final String customerName;
    private final String customerPAN;
    private final Long loanAmount;
    private final String loanType;
    private final Party fintech;
    private final Party bank;



    public LoanState(final String customerName, final String customerMST, final Long loanAmount, final String loanType, final Party fintech, final Party bank) {
        this.customerName = customerName;
        this.customerMST = customerMST;

        this.loanAmount = loanAmount;
        this.loanType = loanType;
        this.fintech = fintech;
        this.bank = bank;
    }

    @Override
    public List<AbstractParty> getParticipants() {
        return Arrays.asList(fintech,bank);
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerMST() {
        return customerMST;
    }

    public Long getLoanAmount() {
        return loanAmount;
    }

    public String getLoanType() {
        return loanType;
    }

    public Party getFintech(){
        return fintech;
    }

    public Party getBank(){
        return bank;
    }
}
