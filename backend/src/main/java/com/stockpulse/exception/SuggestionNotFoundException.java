package com.stockpulse.exception;

public class SuggestionNotFoundException extends RuntimeException {
    public SuggestionNotFoundException(String message) {
        super(message);
    }
    
    public SuggestionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}