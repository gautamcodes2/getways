package com.stripe.getways.events.Handler;

import com.stripe.model.Event;

public interface StripeEventHandler {
    String getEventType();

    void handle(Event event);
}
