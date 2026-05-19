package org.example.dto.payment;

import java.math.BigDecimal;
import org.example.model.payment.PaymentStatus;

public record PaymentResponseDto(
    Long paymentId,
    PaymentStatus status,
    String fromIban,
    String currency,
    BigDecimal amount,
    String creditorName,
    String creditorIban
) {
}
