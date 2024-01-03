package com.template.contracts;

import com.template.states.LoanState;
import java.security.PublicKey;
import java.util.List;
import net.corda.core.contracts.Command;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.contracts.ContractState;
import net.corda.core.transactions.LedgerTransaction;
import org.jetbrains.annotations.NotNull;

// ************
// * Contract *
// ************
public class LoanContract implements Contract {
    // This is used to identify our contract when building a transaction.
    public static final String LOAN_CONTRACT_ID = "com.template.contracts.LoanContract";

    // A transaction is valid if the verify() function of the contract of all the transaction's input and output states
    // does not throw an exception.
    @Override
    public void verify(@NotNull final LedgerTransaction tx) throws IllegalArgumentException {

        if (tx.getCommands().size() != 1) {
            throw new IllegalArgumentException("There can be only one command in a tx");
        }
        Command command = tx.getCommand(0);
        CommandData commandType = command.getValue();
        List<PublicKey> requiredSigners = command.getSigners();

        if (commandType instanceof LoanRequest) {

//    Shape Rules

            if (tx.getInputStates().size() != 0) {
                throw new IllegalArgumentException("There cannot be input states in a LoanRequest tx");
            }
            if (tx.getOutputStates().size() != 1) {
                throw new IllegalArgumentException("Only one loan can be disbursed in a tx");
            }
//    Content Rules

            ContractState outputState = tx.getOutput(0);

            if (!(outputState instanceof LoanState)) {
                throw new IllegalArgumentException("Output state has to be of LoanState");
            }
            LoanState loanState = (LoanState) outputState;
            System.out.println("LoanType received: " + loanState.getLoanType());
            if (!(loanState.getLoanType().equals("Personal"))) {

                throw new IllegalArgumentException("Only Personal Loans can be applied by Fintech");
            }
//    Signer Rules

            PublicKey fintechKey = loanState.getFintech().getOwningKey();

            if ((!requiredSigners.contains(fintechKey))) {
                throw new IllegalArgumentException("Fintech/ Requestor must sign the transaction");
            }
        }
    }

    // Used to indicate the transaction's intent.
    public static class LoanRequest implements CommandData {
    }
}

