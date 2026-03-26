package com.typingdna.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record ThresholdRequest(
        @NotNull(message = "threshold es requerido")
        @DecimalMin(value = "0.0", message = " el valor minimo es 0.0")
        @DecimalMax(value = "1.0", message = " el valor maximo es 1.0")
        Double threshold
) {
}
