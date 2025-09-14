package com.stripe.getways.events;

public final class Events {

    public static final String PAYMENT_INTENT_CREATED = "payment_intent.created";
    public static final String PAYMENT_INTENT_REQUIRES_ACTION = "payment_intent.requires_action";
    public static final String PAYMENT_INTENT_SUCCEEDED = "payment_intent.succeeded";
    public static final String CHECKOUT_SESSION_COMPLETED = "checkout.session.completed";
    public static final String CHECKOUT_SESSION_EXPIRED = "checkout.session.expired";
    public static final String PAYMENT_INTENT_CANCELLED = "payment_intent.canceled";
    public static final String PAYMENT_INTENT_PAYMENT_FAILED = "payment_intent.payment_failed";
    public static final String CHARGE_SUCCEEDED = "charge.succeeded";
    public static final String CHARGE_UPDATED = "charge.updated";
    public static final String CHARGE_FAILED = "charge.failed";


    // Those are not needed now
    public static final String CHECKOUT_SESSION_ASYNC_PAYMENT_FAILED = "checkout.session.async_payment_failed";
    public static final String CHECKOUT_SESSION_ASYNC_PAYMENT_SUCCEEDED = "checkout.session.async_payment_succeeded";
    public static final String PAYMENT_INTENT_PARTIALLY_FUNDED = "payment_intent.partially_funded";
    public static final String PAYMENT_INTENT_PROCESSING = "payment_intent.processing";
}
