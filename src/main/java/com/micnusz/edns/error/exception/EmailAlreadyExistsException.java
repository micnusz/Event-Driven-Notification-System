package com.micnusz.edns.error.exception;

public class EmailAlreadyExistsException extends RuntimeException{
    public EmailAlreadyExistsException(String email) {
        super("Email: " + email + " already exists.");
    }
}
