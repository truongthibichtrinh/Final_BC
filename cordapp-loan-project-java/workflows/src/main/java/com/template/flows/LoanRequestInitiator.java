package com.template.flows;

import co.paralleluniverse.fibers.Suspendable;
import com.template.contracts.LoanContract;
import static com.template.contracts.LoanContract.LOAN_CONTRACT_ID;
import com.template.states.LoanState;
import net.corda.core.flows.FinalityFlow;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.FlowSession;
import net.corda.core.flows.InitiatingFlow;
import net.corda.core.flows.StartableByRPC;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;

// ******************
// * Initiator flow *
// ******************
@InitiatingFlow
@StartableByRPC
public class LoanRequestInitiator extends FlowLogic<SignedTransaction> {

    private String customerName;
    private String customerMST;
    private Long loanAmount;
    private String loanType;
    private Party bank;

    public LoanRequestInitiator(final String customerName, final String customerMST, final Long loanAmount, final String loanType, final Party bank) {
        this.customerName = customerName;
        this.customerMST = customerMST;
        this.loanAmount = loanAmount;
        this.loanType = loanType;
        this.bank = bank;
    }

    private final ProgressTracker progressTracker = new ProgressTracker();

    @Override
    public ProgressTracker getProgressTracker() {
        return progressTracker;
    }

    @Suspendable
    @Override
    public SignedTransaction call() throws FlowException {
        // LoanRequestInitiator flow logic goes here.

//        Check if the txn is initiated by Fintech (MoneyTap)

        if (getOurIdentity().getName().getOrganisation().equals("MoneyTap")) {
            System.out.println("Fintech Identity Verified!");
        } else {
            throw new FlowException("Txn not initiated by Fintech (MoneyTap)");
        }

//        Get Notary identity from network map

        Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);

//        Create the elements for a transaction (Input/ Output states)

        LoanState outputState = new LoanState(customerName, customerPAN, loanAmount, loanType, getOurIdentity(), bank);

//        Transations in Corda are built using Transaction Builder and elements are added to it

        TransactionBuilder txBuilder = new TransactionBuilder(notary).addOutputState(outputState, LOAN_CONTRACT_ID).addCommand(new LoanContract.LoanRequest(), getOurIdentity().getOwningKey());

//        Sign the transation

        SignedTransaction loanReqTxn = getServiceHub().signInitialTransaction(txBuilder);

//        Create a session with Bank

        FlowSession loanReqSession = initiateFlow(bank);

//        Finalize the transaction

        return subFlow(new FinalityFlow(loanReqTxn, loanReqSession));


    }
}
