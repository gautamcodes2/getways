package com.stripe.getways.events.Handler;

import com.stripe.exception.StripeException;
import com.stripe.getways.events.Events;
import com.stripe.getways.service.repository.BalanceTransactionRepositoryService;
import com.stripe.getways.service.repository.PaymentSessionRepositoryService;
import com.stripe.model.BalanceTransaction;
import com.stripe.model.Charge;
import com.stripe.model.Event;
import org.springframework.stereotype.Component;

@Component
public class ChargeSucceededHandler implements StripeEventHandler {

    private final PaymentSessionRepositoryService paymentSessionRepositoryService;
    private final BalanceTransactionRepositoryService balanceTransactionRepositoryService;


    public ChargeSucceededHandler(PaymentSessionRepositoryService paymentSessionRepositoryService, BalanceTransactionRepositoryService balanceTransactionRepositoryService) {
        this.paymentSessionRepositoryService = paymentSessionRepositoryService;
        this.balanceTransactionRepositoryService = balanceTransactionRepositoryService;
    }

    @Override
    public String getEventType() {
        return Events.CHARGE_SUCCEEDED;
    }

    @Override
    public void handle(Event event) {
        event.getDataObjectDeserializer().getObject()
                .filter(Charge.class::isInstance)
                .map(Charge.class::cast)
                .ifPresentOrElse(charge -> {
                    try {
                        persistChargeAndBalanceTransaction(charge);
                    } catch (StripeException e) {
                        throw new RuntimeException(
                                "Failed to persist Stripe charge or balance transaction for event: " + event.getType(), e);
                    }
                }, () -> {
                    throw new IllegalStateException(
                            "Charge object is null for Stripe event: " + event.getType());
                });
    }

    private void persistChargeAndBalanceTransaction(Charge charge) throws StripeException {
        paymentSessionRepositoryService.persistCharge(charge);
        if (charge.getBalanceTransaction() != null) {
            BalanceTransaction balanceTransaction = BalanceTransaction.retrieve(charge.getBalanceTransaction());
            balanceTransactionRepositoryService.persistBalanceTransaction(balanceTransaction);
        }
    }
}
