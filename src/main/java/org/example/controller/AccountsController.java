package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.dto.balance.BalanceResponseDto;
import org.example.dto.transaction.TransactionDto;
import org.example.service.AccountsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountsController {
    private final AccountsService accountService;

    @Operation(
        summary = "Returns the current account balance",
        description = "Returns current balance for the specified IBAN accountId."
    )
    @GetMapping("/{accountId}/balance")
    public BalanceResponseDto getBalance(@PathVariable String accountId) {
        return accountService.getBalance(accountId);
    }

    @Operation(
        summary = "Get transaction history",
        description = "Returns the last 10 transactions for the specified IBAN accountId."
    )
    @GetMapping("/{accountId}/transactions")
    public List<TransactionDto> getTransactionHistory(@PathVariable String accountId) {
        return accountService.getTransactionHistory(accountId);
    }
}
