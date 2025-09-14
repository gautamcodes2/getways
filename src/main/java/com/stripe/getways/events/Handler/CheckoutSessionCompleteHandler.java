package com.stripe.getways.events.Handler;

import com.stripe.getways.events.Events;
import com.stripe.getways.service.repository.PaymentSessionRepositoryService;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import org.springframework.stereotype.Component;

@Component
public class CheckoutSessionCompleteHandler implements StripeEventHandler {

    private final PaymentSessionRepositoryService paymentSessionRepositoryService;

    public CheckoutSessionCompleteHandler(PaymentSessionRepositoryService paymentSessionRepositoryService) {
        this.paymentSessionRepositoryService = paymentSessionRepositoryService;
    }

    @Override
    public String getEventType() {
        return Events.CHECKOUT_SESSION_COMPLETED;
    }

    @Override
    public void handle(Event event) {
        Session session = (Session) event.getDataObjectDeserializer().getObject()
                .orElseThrow(() -> new IllegalStateException("Stripe session object is null for event type: " + event.getType()));

        paymentSessionRepositoryService.persistSession(session);
    }
}
