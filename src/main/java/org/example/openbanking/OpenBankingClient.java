package org.example.openbanking;

import java.util.List;
import org.example.dto.external.ExternalAccountResponseDto;
import org.example.dto.external.ExternalPaymentResponseDto;
import org.example.dto.external.ExternalTransactionResponseDto;
import org.example.dto.payment.PaymentRequestDto;

public interface OpenBankingClient {
    ExternalAccountResponseDto findAccountById(String accountId);

    List<ExternalTransactionResponseDto> getTransactionHistory(String accountId);

    ExternalPaymentResponseDto initiatePayment(PaymentRequestDto requestDto);
}
