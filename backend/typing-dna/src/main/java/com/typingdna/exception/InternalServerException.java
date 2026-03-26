package com.typingdna.exception;

import org.springframework.http.HttpStatus;

public class InternalServerException extends AppException{
    public InternalServerException() {
        super("Error serializando vector", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
