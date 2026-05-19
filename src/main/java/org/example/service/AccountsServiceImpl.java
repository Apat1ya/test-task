package org.example.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.dto.balance.BalanceResponseDto;
import org.example.dto.external.ExternalAccountResponseDto;
import org.example.dto.external.ExternalBalanceResponseDto;
import org.example.dto.external.ExternalTransactionResponseDto;
import org.example.dto.transaction.TransactionDto;
import org.example.mapper.AccountMapper;
import org.example.mapper.TransactionalMapper;
import org.example.openbanking.OpenBankingClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountsServiceImpl implements AccountsService {
    private final OpenBankingClient openBankingClient;
    private final AccountMapper accountMapper;
    private final TransactionalMapper transactionalMapper;

    @Override
    public BalanceResponseDto getBalance(String accountId) {
        ExternalAccountResponseDto responseDto = openBankingClient.findAccountById(accountId);
        ExternalBalanceResponseDto balance = responseDto.balance();
        return accountMapper.toResponseDto(balance);
    }

    @Override
    public List<TransactionDto> getTransactionHistory(String accountId) {
        // Спрощена реалізація: у специфікації використовується пагінація, але тут її опущено, оскільки це тестове завдання
        List<ExternalTransactionResponseDto> transactions = openBankingClient.getTransactionHistory(accountId);
        int fromIndex = Math.max(0, transactions.size() - 10);
        return transactions.subList(fromIndex, transactions.size()).stream()
            .map(transactionalMapper::toTransactionDto)
            .toList();
    }
}
