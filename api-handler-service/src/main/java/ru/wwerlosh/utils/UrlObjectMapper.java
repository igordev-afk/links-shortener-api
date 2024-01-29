package ru.wwerlosh.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import ru.wwerlosh.entities.Url;

import com.fasterxml.jackson.core.JsonProcessingException;

@Component
public class UrlObjectMapper {

    private final ObjectMapper objectMapper;

    public UrlObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String convertToJson(Url url) {
        try {
            return objectMapper.writeValueAsString(url);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting Url to JSON", e);
        }
    }

    public Url convertToUrl(String jsonString) {
        try {
            return objectMapper.readValue(jsonString, Url.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JSON to Url", e);
        }
    }
}

