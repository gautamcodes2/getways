package com.stripe.getways.utility;

public class StringUtil {

    public static String safeTrim(String input) {
        if (input == null) {
            return null;
        }
        String trimmed = input.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
