package com.typingdna.exception;

import org.springframework.http.HttpStatus;

public class BiometricProfileNotFoundException extends AppException {
    public BiometricProfileNotFoundException(Long usuarioId) {
        super("Perfil biometrico no encontrado para el usuario: " + usuarioId, HttpStatus.NOT_FOUND);
    }
}
