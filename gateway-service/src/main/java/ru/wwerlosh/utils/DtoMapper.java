package ru.wwerlosh.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.wwerlosh.controllers.dto.Response;
import ru.wwerlosh.controllers.dto.UrlRequest;
import ru.wwerlosh.dao.request.SignInRequest;
import ru.wwerlosh.dao.request.SignUpRequest;
import ru.wwerlosh.dao.response.JwtAuthenticationDTO;

@Component
public class DtoMapper {

    private final ObjectMapper objectMapper;

    private final ObjectMapper urlResponseObjectMapper;

    public DtoMapper(ObjectMapper objectMapper,
                     @Qualifier("urlResponseObjectMapper") ObjectMapper urlResponseObjectMapper) {
        this.objectMapper = objectMapper;
        this.urlResponseObjectMapper = urlResponseObjectMapper;
    }

    public String convertSignupRequestToJson(SignUpRequest request) {
        try {
            return objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting SignUpRequest to JSON", e);
        }
    }

    public String convertSigninRequestToJson(SignInRequest request) {
        try {
            return objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting SignInRequest to JSON", e);
        }
    }

    public String convertUrlRequestToJson(UrlRequest request) {
        try {
            return objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting UrlRequest to JSON", e);
        }
    }

    public JwtAuthenticationDTO convertToJwtObject(String jsonString) {
        try {
            return objectMapper.readValue(jsonString, JwtAuthenticationDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JSON to JwtObject", e);
        }
    }

    public Response convertToResponse(String jsonString) {
        try {
            return urlResponseObjectMapper.readerFor(Response.class).readValue(jsonString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JSON to Response", e);
        }
    }
}
