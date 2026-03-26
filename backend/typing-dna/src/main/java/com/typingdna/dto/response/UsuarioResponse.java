package com.typingdna.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UsuarioResponse(
        Long id,
        String username,
        String email,
        Boolean isActive,
        LocalDateTime createdAt
) {
}
