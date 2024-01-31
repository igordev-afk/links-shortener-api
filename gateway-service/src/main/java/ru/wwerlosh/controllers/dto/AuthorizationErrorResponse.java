package ru.wwerlosh.controllers.dto;

public class AuthorizationErrorResponse extends ErrorResponse {

    private int statusCode;

    public AuthorizationErrorResponse(String error, int statusCode) {
        super(error);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
