package com.typingdna.exception;

import org.springframework.http.HttpStatus;

public class UsernameAlreadyExistException extends AppException{
    public UsernameAlreadyExistException(String username) {
        super("El username '" + username + "' ya está en uso", HttpStatus.CONFLICT);
    }
}
