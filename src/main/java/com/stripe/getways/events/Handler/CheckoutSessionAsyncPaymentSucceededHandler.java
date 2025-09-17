package com.stripe.getways.events.Handler;

import com.stripe.getways.events.Events;
import com.stripe.getways.service.repository.PaymentSessionRepositoryService;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import org.springframework.stereotype.Component;

@Component
public class CheckoutSessionAsyncPaymentSucceededHandler implements StripeEventHandler {

    private final PaymentSessionRepositoryService paymentSessionRepositoryService;

    public CheckoutSessionAsyncPaymentSucceededHandler(PaymentSessionRepositoryService paymentSessionRepositoryService) {
        this.paymentSessionRepositoryService = paymentSessionRepositoryService;
    }

    @Override
    public String getEventType() {
        return Events.CHECKOUT_SESSION_ASYNC_PAYMENT_SUCCEEDED;
    }

    @Override
    public void handle(Event event) {
        Session session = (Session) event.getDataObjectDeserializer().getObject()
                .orElseThrow(() -> new IllegalStateException("Stripe session object is null for event type: " + event.getType()));

        var savedEntity = paymentSessionRepositoryService.persistSession(session);
    }
}