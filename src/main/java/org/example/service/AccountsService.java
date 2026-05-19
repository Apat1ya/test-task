package org.example.service;

import java.util.List;
import org.example.dto.balance.BalanceResponseDto;
import org.example.dto.transaction.TransactionDto;

public interface AccountsService {
    BalanceResponseDto getBalance(String accountId);

    List<TransactionDto> getTransactionHistory(String accountId);
}
