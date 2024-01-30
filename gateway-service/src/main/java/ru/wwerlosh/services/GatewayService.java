package ru.wwerlosh.services;

import org.springframework.stereotype.Service;
import ru.wwerlosh.configs.HttpClient;
import ru.wwerlosh.dao.request.SignInRequest;
import ru.wwerlosh.dao.request.SignUpRequest;
import ru.wwerlosh.dao.response.JwtAuthenticationResponse;

@Service
public class GatewayService {

    private final HttpClient httpClient;

    public GatewayService(HttpClient httpClient) {
        this.httpClient = httpClient;
    }


    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        return httpClient.signUpClient(request);
    }

    public JwtAuthenticationResponse signIn(SignInRequest request) {
        return httpClient.signInClient(request);
    }
}
