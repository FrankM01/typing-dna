package com.typingdna.dto.response;

import lombok.Builder;

@Builder
public record StatsResponse(
        int totalAttempts,
        double successRate,
        double averageScore
) {
}
