package org.example.technihongo.exception;

public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(String message){
        super(message);
    }
}
