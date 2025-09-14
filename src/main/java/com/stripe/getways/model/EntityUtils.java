package com.stripe.getways.model;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class EntityUtils {

    public static <T> void setIfAbsent(Supplier<T> getter, Consumer<T> setter, T value) {
        if (getter.get() == null && value != null) {
            setter.accept(value);
        }
    }
}
