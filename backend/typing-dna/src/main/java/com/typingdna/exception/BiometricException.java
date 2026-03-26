package com.typingdna.exception;

import org.springframework.http.HttpStatus;

public class BiometricException extends AppException{
    public BiometricException(Integer samplesCount) {
        super("El perfil aun no esta completo. Muestras actuales: " + samplesCount, HttpStatus.BAD_REQUEST);
    }
}
