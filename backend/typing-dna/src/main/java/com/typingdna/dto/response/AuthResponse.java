package com.typingdna.dto.response;

import lombok.Builder;

@Builder
public record AuthResponse(
        String token,
        String username,
        String email
) {
}
