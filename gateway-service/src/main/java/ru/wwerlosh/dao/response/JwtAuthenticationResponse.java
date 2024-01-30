package ru.wwerlosh.dao.response;

import java.io.Serializable;

public class JwtAuthenticationResponse {
    private String token;

    public JwtAuthenticationResponse() {
    }

    public JwtAuthenticationResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
