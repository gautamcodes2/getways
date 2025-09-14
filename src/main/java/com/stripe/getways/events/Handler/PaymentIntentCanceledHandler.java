package com.stripe.getways.events.Handler;


import com.stripe.getways.events.Events;
import com.stripe.getways.service.repository.PaymentSessionRepositoryService;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import org.springframework.stereotype.Component;

@Component
public class PaymentIntentCanceledHandler implements StripeEventHandler {

    private final PaymentSessionRepositoryService paymentSessionRepositoryService;

    public PaymentIntentCanceledHandler(PaymentSessionRepositoryService paymentSessionRepositoryService) {
        this.paymentSessionRepositoryService = paymentSessionRepositoryService;
    }

    @Override
    public String getEventType() {
        return Events.PAYMENT_INTENT_CANCELLED;
    }

    @Override
    public void handle(Event event) {
        PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);

        if (intent != null) {
            paymentSessionRepositoryService.persistPaymentIntent(intent);
        } else {
            System.out.println("Intent object is null for event: " + event.getType());
        }
    }
}

