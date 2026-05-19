package org.example.dto.external;

import java.math.BigDecimal;

public record ExternalTransactionResponseDto(
    Long id,
    String creditorName,
    String creditorIban,
    String currency,
    BigDecimal amount
) {
}
