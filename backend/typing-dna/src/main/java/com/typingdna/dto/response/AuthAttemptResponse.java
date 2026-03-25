package com.typingdna.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AuthAttemptResponse(
         Long id,
         Double similarityScore,
         Boolean success,
         String ipAddress,
         LocalDateTime createdAt
) {
}
