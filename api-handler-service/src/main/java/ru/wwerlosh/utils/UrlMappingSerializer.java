package ru.wwerlosh.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.wwerlosh.entities.UrlMapping;

@Component
public class UrlMappingSerializer {

    private static final Logger logger = LoggerFactory.getLogger(UrlMappingSerializer.class);

    private final ObjectMapper objectMapper;

    public UrlMappingSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String convertToJson(UrlMapping url) {
        try {
            String jsonString = objectMapper.writeValueAsString(url);
            logger.debug("Converted Url to JSON: {}", jsonString);
            return jsonString;
        } catch (JsonProcessingException e) {
            logger.error("Error converting Url to JSON", e);
            throw new RuntimeException("Error converting Url to JSON", e);
        }
    }

    public UrlMapping convertToUrlMapping(String jsonString) {
        try {
            UrlMapping url = objectMapper.readValue(jsonString, UrlMapping.class);
            logger.debug("Converted JSON to Url: {}", url);
            return url;
        } catch (JsonProcessingException e) {
            logger.error("Error converting JSON to Url", e);
            throw new RuntimeException("Error converting JSON to Url", e);
        }
    }
}

