package com.stripe.getways.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class JsonUtils {
    private final static ObjectMapper mapper = new ObjectMapper();

    public static Map<String, Object> toCleanMap(String json) {
        try {
            Map<String, Object> map = mapper.readValue(json, new TypeReference<>() {
            });
            map.entrySet().removeIf(entry -> entry.getValue() == null);
            return map;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON parsing failed", e);
        }
    }
}
