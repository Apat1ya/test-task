package org.example.dto.balance;

import java.math.BigDecimal;

public record BalanceResponseDto(
    String iban,
    BigDecimal balance,
    String currency
) {
}
