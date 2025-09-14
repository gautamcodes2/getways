package com.stripe.getways.events.Handler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class StripeEventHandlerRegistry {

    private final Map<String, StripeEventHandler> handlerMap;

    public StripeEventHandlerRegistry(List<StripeEventHandler> handlers) {
        this.handlerMap = handlers.stream()
                .collect(Collectors.toMap(
                        StripeEventHandler::getEventType,
                        h -> h
                ));
    }

    public StripeEventHandler getHandler(String eventType) {
        return handlerMap.get(eventType);
    }
}
