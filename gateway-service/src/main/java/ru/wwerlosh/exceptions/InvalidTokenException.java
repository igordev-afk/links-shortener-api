package ru.wwerlosh.exceptions;

public class InvalidTokenException extends RuntimeException {

    private String jwt;

    public InvalidTokenException(String message, String jwt) {
        super(message);
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }
}
