package com.stripe.getways.stripe;

import com.stripe.exception.StripeException;
import com.stripe.getways.dto.input.PaymentRequest;
import com.stripe.model.checkout.Session;
import com.stripe.net.RequestOptions;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.LineItem;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData.ProductData;
import com.stripe.param.checkout.SessionCreateParams.PaymentIntentData;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class StripeClient {

    public Session createCheckout(PaymentRequest paymentRequest) throws StripeException {

        String successUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/9/90/Labrador_Retriever_portrait.jpg/330px-Labrador_Retriever_portrait.jpg";
        String cancelUrl = "https://images.squarespace-cdn.com/content/v1/607f89e638219e13eee71b1e/1684821560422-SD5V37BAG28BURTLIXUQ/michael-sum-LEpfefQf4rU-unsplash.jpg?format=2500w";

        var expires = Instant.now().getEpochSecond() + 1800;
        System.out.println("Expires: " + expires);
        PaymentIntentData paymentIntentData = PaymentIntentData.builder()
                .putMetadata("orderId", paymentRequest.getOrderId())
                .build();


        SessionCreateParams params = SessionCreateParams.builder()
                .setPaymentIntentData(paymentIntentData)
                .setCustomerEmail(paymentRequest.getEmail())
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .addAllPaymentMethodType(
                        List.of(SessionCreateParams.PaymentMethodType.CASHAPP,
                                SessionCreateParams.PaymentMethodType.CARD,
                                SessionCreateParams.PaymentMethodType.AMAZON_PAY,
                                SessionCreateParams.PaymentMethodType.US_BANK_ACCOUNT))
                .addLineItem(
                        LineItem.builder()
                                .setPriceData(
                                        PriceData.builder()
                                                .setCurrency("USD")
                                                .setUnitAmount(
                                                        paymentRequest.getAmount())
                                                .setProductData(
                                                        ProductData.builder()
                                                                .setName("Test Product")
                                                                .build())
                                                .build())
                                .setQuantity(1L)
                                .build())
                .setExpiresAt(expires)
                .build();

        RequestOptions requestOptions = RequestOptions.builder().build();

        return Session.create(params, requestOptions);
    }
}
