package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.external.ExternalPaymentResponseDto;
import org.example.dto.payment.PaymentRequestDto;
import org.example.dto.payment.PaymentResponseDto;
import org.example.mapper.PaymentMapper;
import org.example.model.payment.PaymentEntity;
import org.example.openbanking.OpenBankingClient;
import org.example.repository.PaymentRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {
    private final OpenBankingClient openBankingClient;
    private final PaymentMapper paymentMapper;
    private final PaymentRequestRepository repository;

    @Override
    public PaymentResponseDto initiatePayment(PaymentRequestDto requestDto) {
        PaymentEntity paymentEntity = paymentMapper.toEntity(requestDto);
        repository.save(paymentEntity);

        ExternalPaymentResponseDto externalPayment = openBankingClient.initiatePayment(requestDto);
        return paymentMapper.toPaymentResponseDto(externalPayment);
    }
}
