package org.example.dto.external;

import java.math.BigDecimal;

public record ExternalBalanceResponseDto(
    String iban,
    BigDecimal balance,
    String currency
) {
}
