package org.example.dto.payment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record PaymentRequestDto(
    Long paymentId,
    @NotBlank(message = "fromIban must not be blank")
    @Size(min = 29, max = 29)
    String fromIban,
    @NotNull
    String currency,
    @Positive
    @NotNull
    BigDecimal amount,
    @NotNull
    String creditorName,
    @NotNull
    @Size(min = 29, max = 29)
    String creditorIban
) {
}
