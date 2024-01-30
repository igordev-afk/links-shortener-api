package ru.wwerlosh.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import ru.wwerlosh.dao.request.SignInRequest;
import ru.wwerlosh.dao.request.SignUpRequest;
import ru.wwerlosh.dao.response.JwtAuthenticationResponse;

@Component
public class DtoMapper {

    private final ObjectMapper objectMapper;

    public DtoMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String convertSignupRequestToJson(SignUpRequest request) {
        try {
            return objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting Url to JSON", e);
        }
    }

    public String convertSigninRequestToJson(SignInRequest request) {
        try {
            return objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting Url to JSON", e);
        }
    }

    public JwtAuthenticationResponse convertToJwtObject(String jsonString) {
        try {
            return objectMapper.readValue(jsonString, JwtAuthenticationResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JSON to Url", e);
        }
    }

}
