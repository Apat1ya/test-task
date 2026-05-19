package org.example.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;
import org.example.dto.balance.BalanceResponseDto;
import org.example.dto.transaction.TransactionDto;
import org.example.service.AccountsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.json.JsonMapper;

@SpringBootTest
@AutoConfigureMockMvc
class AccountsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper objectMapper;

    @MockitoBean
    private AccountsService accountsService;

    @Test
    void getBalance_ok() throws Exception {
        BalanceResponseDto expected = new BalanceResponseDto(
            "UA123",
            BigDecimal.valueOf(123.50),
            "UAH"
        );

        when(accountsService.getBalance("UA123"))
            .thenReturn(expected);

        MvcResult mvcResult = mockMvc.perform(get("/accounts/UA123/balance")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
        BalanceResponseDto actual = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(),
            BalanceResponseDto.class
        );

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void getTransactionHistory_ok() throws Exception {
        String accountId = "UA123";

        TransactionDto t1 = new TransactionDto(
            1L,
            "Amazon",
            "UA11111111111111111111111111",
            "UAH",
            BigDecimal.valueOf(100.5)
        );

        TransactionDto t2 = new TransactionDto(
            2L,
            "Rozetka",
            "UA22222222222222222222222222",
            "UAH",
            BigDecimal.valueOf(250)
        );

        List<TransactionDto> expected = List.of(t1, t2);

        when(accountsService.getTransactionHistory(accountId))
            .thenReturn(expected);

        MvcResult mvcResult = mockMvc.perform(get("/accounts/{accountId}/transactions", accountId)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        List<TransactionDto> actual = List.of(objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(),
            TransactionDto[].class
        ));

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        verify(accountsService).getTransactionHistory(accountId);
    }
}
