package com.typingdna.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "username es requerido")
        String username,

        @NotBlank(message = "password es requerido")
        String password
) {
}
