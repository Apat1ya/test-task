package org.example.mapper;

import org.example.config.MapperConfig;
import org.example.dto.balance.BalanceResponseDto;
import org.example.dto.external.ExternalBalanceResponseDto;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface AccountMapper {
    BalanceResponseDto toResponseDto(ExternalBalanceResponseDto responseDto);
}
