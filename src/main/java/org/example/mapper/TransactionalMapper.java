package org.example.mapper;

import org.example.config.MapperConfig;
import org.example.dto.external.ExternalTransactionResponseDto;
import org.example.dto.transaction.TransactionDto;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface TransactionalMapper {
    TransactionDto toTransactionDto(ExternalTransactionResponseDto responseDto);
}
