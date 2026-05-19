package org.example.dto.transaction;

import java.math.BigDecimal;

public record TransactionDto(
    Long id,
    String creditorName,
    String creditorIban,
    String currency,
    BigDecimal amount
) {
}
