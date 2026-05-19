package org.example.dto.external;

import java.math.BigDecimal;
import org.example.model.payment.PaymentStatus;

public record ExternalPaymentResponseDto(
    Long paymentId,
    PaymentStatus status,
    String fromIban,
    String currency,
    BigDecimal amount,
    String creditorName,
    String creditorIban
) {
}
