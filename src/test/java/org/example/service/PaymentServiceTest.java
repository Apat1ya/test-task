package org.example.service;

import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;
import org.example.dto.payment.PaymentRequestDto;
import org.example.dto.payment.PaymentResponseDto;
import org.example.model.payment.PaymentEntity;
import org.example.model.payment.PaymentStatus;
import org.example.repository.PaymentRequestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;

@SpringBootTest
@Transactional
@EnableWireMock(@ConfigureWireMock(
    baseUrlProperties = "external-bank.base-url",
    portProperties = "external-bank.port"
))
class PaymentServiceTest {
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentRequestRepository repository;

    @Test
    void initiatePayment_ok() {
        stubFor(post("/payments/initiate")
            .willReturn(okJson("""
                {
                  "paymentId": 123,
                  "status": "SUCCESS",
                  "fromIban": "UA123",
                  "currency": "UAH",
                  "amount": 1234,
                  "creditorName": "John",
                  "creditorIban": "UA111122223333"
                }""")));

        PaymentRequestDto requestDto = new PaymentRequestDto(
            123L,
            "UA123",
            "UAH",
            BigDecimal.valueOf(1500.50),
            "John",
            "UA111122223333"
        );

        PaymentResponseDto result = paymentService.initiatePayment(requestDto);

        assertEquals(123L, result.paymentId());
        assertEquals(PaymentStatus.SUCCESS, result.status());
        assertEquals("UA123", result.fromIban());
        assertEquals("UAH", result.currency());
        assertEquals(0, BigDecimal.valueOf(1234).compareTo(result.amount()));
        assertEquals("John", result.creditorName());
        assertEquals("UA111122223333", result.creditorIban());

        verify(postRequestedFor(urlEqualTo("/payments/initiate")));
    }

    @Test
    void initiatePayment_shouldSaveRequest() {
        stubFor(post("/payments/initiate")
            .willReturn(okJson("""
                {
                  "paymentId": 1,
                  "status": "SUCCESS",
                  "fromIban": "UA111122223333",
                  "currency": "UAH",
                  "amount": "1500.50",
                  "creditorName": "John",
                  "creditorIban": "UA444455556666"
                }""")));

        PaymentRequestDto requestDto = new PaymentRequestDto(
            null,
            "UA111122223333",
            "UAH",
            BigDecimal.valueOf(1500.50),
            "John",
            "UA444455556666"
        );

        PaymentResponseDto result = paymentService.initiatePayment(requestDto);

        assertEquals(1, result.paymentId());
        assertEquals(PaymentStatus.SUCCESS, result.status());
        assertEquals("UA111122223333", result.fromIban());
        assertEquals("UAH", result.currency());
        assertEquals(0, BigDecimal.valueOf(1500.5).compareTo(result.amount()));
        assertEquals("John", result.creditorName());
        assertEquals("UA444455556666", result.creditorIban());

        List<PaymentEntity> savedRequests = repository.findAll();
        assertEquals(1, savedRequests.size());
        PaymentEntity saved = savedRequests.getFirst();
        assertEquals("UA111122223333", saved.getFromIban());
        assertEquals("UAH", saved.getCurrency());
        assertEquals(0, BigDecimal.valueOf(1500.5).compareTo(saved.getAmount()));
        assertEquals("John", saved.getCreditorName());
        assertEquals("UA444455556666", saved.getCreditorIban());

        verify(postRequestedFor(urlEqualTo("/payments/initiate")));
    }
}
