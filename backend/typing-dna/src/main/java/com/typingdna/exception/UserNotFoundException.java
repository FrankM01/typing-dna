package com.typingdna.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends AppException{
    public UserNotFoundException(String username) {
        super("Usuario '" + username + "' no encontrado", HttpStatus.NOT_FOUND);
    }
}
