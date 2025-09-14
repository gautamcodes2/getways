package com.stripe.getways.events.Handler;

import com.stripe.getways.events.Events;
import com.stripe.getways.service.repository.PaymentSessionRepositoryService;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import org.springframework.stereotype.Component;

@Component
public class CheckoutSessionExpiredHandler implements StripeEventHandler {

    private final PaymentSessionRepositoryService paymentSessionRepositoryService;

    public CheckoutSessionExpiredHandler(PaymentSessionRepositoryService paymentSessionRepositoryService) {
        this.paymentSessionRepositoryService = paymentSessionRepositoryService;
    }

    @Override
    public String getEventType() {
        return Events.CHECKOUT_SESSION_EXPIRED;
    }

    @Override
    public void handle(Event event) {
        var session = (Session) event
                .getDataObjectDeserializer()
                .getObject().orElse(null);

        if (session != null) {
            paymentSessionRepositoryService.persistSession(session);
        } else {
            System.out.println("Session object is null for event: " + event.getType());
        }
    }
}
