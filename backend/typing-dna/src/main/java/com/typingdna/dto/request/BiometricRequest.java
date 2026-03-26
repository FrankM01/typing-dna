package com.typingdna.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record BiometricRequest(
        @NotNull(message = "dwell vector es requerido")
        List<Double> dwellVector,

        @NotNull(message = "flight vector es requerido")
        List<Double> flightVector
) {
}
