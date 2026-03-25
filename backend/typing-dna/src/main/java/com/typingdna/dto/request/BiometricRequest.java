package com.typingdna.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record BiometricRequest(
        @NotNull
        List<Double> dwellVector,

        @NotNull
        List<Double> flightVector
) {
}
