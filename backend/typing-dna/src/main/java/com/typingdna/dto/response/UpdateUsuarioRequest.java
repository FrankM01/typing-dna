package com.typingdna.dto.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUsuarioRequest(
        @Email(message = "Email inválido")
        String email,

        @Size(min = 6, message = "Mínimo 6 caracteres")
        String password
) {
}
