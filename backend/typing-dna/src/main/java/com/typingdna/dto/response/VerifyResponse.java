package com.typingdna.dto.response;

import lombok.Builder;

@Builder
public record VerifyResponse(
        double score,
        boolean success,
        String message
) {
}
