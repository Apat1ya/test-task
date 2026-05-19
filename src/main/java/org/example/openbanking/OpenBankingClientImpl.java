package org.example.openbanking;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.dto.external.ExternalAccountResponseDto;
import org.example.dto.external.ExternalPaymentResponseDto;
import org.example.dto.external.ExternalTransactionResponseDto;
import org.example.dto.payment.PaymentRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class OpenBankingClientImpl implements OpenBankingClient {
    private final WebClient webClient;

    @Override
    public ExternalAccountResponseDto findAccountById(String accountId) {
        return webClient.get()
            .uri("/accounts/{accountId}/balance", accountId)
            .retrieve()
            .bodyToMono(ExternalAccountResponseDto.class)
            .block();
    }

    @Override
    public List<ExternalTransactionResponseDto> getTransactionHistory(String accountId) {
        return webClient.get()
            .uri("/accounts/{accountId}/transactions", accountId)
            .retrieve()
            .bodyToFlux(ExternalTransactionResponseDto.class)
            .collectList()
            .block();
    }

    @Override
    public ExternalPaymentResponseDto initiatePayment(PaymentRequestDto requestDto) {
        return webClient.post()
            .uri("/payments/initiate")
            .bodyValue(requestDto)
            .retrieve()
            .bodyToMono(ExternalPaymentResponseDto.class)
            .block();
    }
}
