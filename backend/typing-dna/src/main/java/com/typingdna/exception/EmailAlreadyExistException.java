package com.typingdna.exception;

import org.springframework.http.HttpStatus;

public class EmailAlreadyExistException extends AppException{
    public EmailAlreadyExistException(String email) {
        super("El email '" + email + "' ya está en uso", HttpStatus.CONFLICT);
    }
}
