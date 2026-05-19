package org.example.mapper;

import org.example.config.MapperConfig;
import org.example.dto.external.ExternalPaymentResponseDto;
import org.example.dto.payment.PaymentRequestDto;
import org.example.dto.payment.PaymentResponseDto;
import org.example.model.payment.PaymentEntity;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface PaymentMapper {
    PaymentResponseDto toPaymentResponseDto(ExternalPaymentResponseDto responseDto);

    PaymentEntity toEntity(PaymentRequestDto requestDto);
}
