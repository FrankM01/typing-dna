package com.typingdna.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "El username es requerido")
        @Size(min = 3, max = 50, message = "el tamaño debe de estar entre 3 y 50 caracteres")
        String username,

        @NotBlank(message = "El email es requerido")
        @Email(message = "Email inválido")
        String email,

        @NotBlank(message = "La contraseña es requerida")
        @Size(min = 6, message = "Mínimo 6 caracteres")
        String password
) {
}
