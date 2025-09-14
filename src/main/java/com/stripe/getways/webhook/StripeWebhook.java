package com.stripe.getways.webhook;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.getways.events.Handler.StripeEventHandler;
import com.stripe.getways.events.Handler.StripeEventHandlerRegistry;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/payment")
public class StripeWebhook {

    private final StripeEventHandlerRegistry registry;

    @Value("${stripe.api.signing-secret}")
    private String endpointSecret;

    public StripeWebhook(StripeEventHandlerRegistry registry) {
        this.registry = registry;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(HttpServletRequest httpServletRequest,
                                                      @RequestHeader("Stripe-Signature") String sigHeader,
                                                      @RequestBody String payload) throws IOException {

        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            throw new RuntimeException("Signature cannot be verified: " + e.getMessage());
        }


//        event.getDataObjectDeserializer().getObject().ifPresent(e -> {
//            if (e instanceof Charge) {
//                System.out.println(" ");
//                System.out.println(" Charge ");
//                System.out.println(((Charge) e).getStatus());
//                System.out.println(((Charge) e));
//            } else if (e instanceof PaymentIntent) {
//                System.out.println(" ");
//                System.out.println(" PaymentIntent ");
//                System.out.println(((PaymentIntent) e).getStatus());
//                System.out.println(((PaymentIntent) e));
//            } else if (e instanceof Session) {
//                System.out.println(" ");
//                System.out.println(" Session ");
//                System.out.println(((Session) e).getStatus());
//                System.out.println(((Session) e));
//            }
//        });

        System.out.println(" ");
        System.out.println("⚠️ event received " + event.getType());
//        System.out.println(event);
        System.out.println(" ");

        StripeEventHandler handler = registry.getHandler(event.getType());
        if (handler != null) {
            handler.handle(event);
        } else {
            System.out.println(" ");
            System.out.println("⚠️ No handler for event: " + event.getType());
            System.out.println(event);
            System.out.println(" ");
        }
        return ResponseEntity.ok().build();
    }
}
