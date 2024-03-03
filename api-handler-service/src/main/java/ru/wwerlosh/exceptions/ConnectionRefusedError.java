package ru.wwerlosh.exceptions;

public class ConnectionRefusedError extends RuntimeException {

    public ConnectionRefusedError(Throwable cause) {
        super(cause);
    }

    public ConnectionRefusedError(String message) {
        super(message);
    }
}
