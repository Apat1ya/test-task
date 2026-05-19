package org.example.dto.external;

public record ExternalAccountResponseDto(
    String iban,
    ExternalBalanceResponseDto balance
) {
}
