package com.typingdna.dto.response;

import lombok.Builder;

@Builder
public record EnrollResponse(
        int samplesCount,
        int requiredSamples,
        boolean complete,
        String message
) {
}
