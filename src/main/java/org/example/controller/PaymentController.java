package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.payment.PaymentRequestDto;
import org.example.dto.payment.PaymentResponseDto;
import org.example.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @Operation(
        summary = "Initiate IBAN-to-IBAN payment",
        description = """
            Creates a payment transfer between two IBAN accounts. \
            Requires source IBAN, destination IBAN, amount and currency. \
            The request is forwarded to an external banking API for processing."""
    )
    @PostMapping("/initiate")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponseDto initiatePayment(@RequestBody @Valid PaymentRequestDto requestDto) {
        return paymentService.initiatePayment(requestDto);
    }
}
