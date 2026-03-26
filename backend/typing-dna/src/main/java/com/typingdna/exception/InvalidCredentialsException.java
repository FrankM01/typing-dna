package com.typingdna.exception;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends AppException{
    public InvalidCredentialsException() {
        super("Usuario o contraseña incorrectos", HttpStatus.UNAUTHORIZED);
    }
}
