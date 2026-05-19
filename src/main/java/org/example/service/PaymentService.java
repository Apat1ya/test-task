package org.example.service;

import org.example.dto.payment.PaymentRequestDto;
import org.example.dto.payment.PaymentResponseDto;

public interface PaymentService {
    PaymentResponseDto initiatePayment(PaymentRequestDto requestDto);
}
