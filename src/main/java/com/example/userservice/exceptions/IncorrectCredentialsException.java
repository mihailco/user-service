package com.example.userservice.exceptions;

public class IncorrectCredentialsException extends Exception {
    public IncorrectCredentialsException(String message) {
        super(message);
    }
}
