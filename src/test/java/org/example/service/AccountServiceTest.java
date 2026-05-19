package org.example.service;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.example.openbanking.OpenBankingClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;

@SpringBootTest
@EnableWireMock(@ConfigureWireMock(
    baseUrlProperties = "external-bank.base-url",
    portProperties = "external-bank.port"
))
class AccountServiceTest {

    @Autowired
    private OpenBankingClient client;
    @Autowired
    private AccountsService accountsService;

    @Test
    void getBalance() {
        stubFor(get("/accounts/UA123/balance")
            .willReturn(okJson("""
                {
                  "iban": "UA123",
                  "balance": {
                    "iban": "UA123",
                    "balance": "1500.50",
                    "currency": "UAH"
                  }
                }""")));

        var result = accountsService.getBalance("UA123");

        assertEquals("UA123", result.iban());
        assertEquals(0, BigDecimal.valueOf(1500.5).compareTo(result.balance()));
        assertEquals("UAH", result.currency());
        verify(getRequestedFor(urlEqualTo("/accounts/UA123/balance")));
    }

    @Test
    void getTransactionHistory() {
        var transactions = """
            [
              { "id": 0, "creditorName": "John", "creditorIban": "UA111122223333", "currency": "UAH", "amount": 100 },
              { "id": 1, "creditorName": "Alice", "creditorIban": "UA114157523333", "currency": "UAH", "amount": 200 },
              { "id": 2, "creditorName": "John", "creditorIban": "UA111122223333", "currency": "UAH", "amount": 300 },
              { "id": 3, "creditorName": "Alice", "creditorIban": "UA114157523333", "currency": "UAH", "amount": 400 },
              { "id": 4, "creditorName": "John", "creditorIban": "UA111122223333", "currency": "UAH", "amount": 500 },
              { "id": 5, "creditorName": "Alice", "creditorIban": "UA114157523333", "currency": "UAH", "amount": 600 },
              { "id": 6, "creditorName": "John", "creditorIban": "UA111122223333", "currency": "UAH", "amount": 700 },
              { "id": 7, "creditorName": "Alice", "creditorIban": "UA114157523333", "currency": "UAH", "amount": 800 },
              { "id": 8, "creditorName": "John", "creditorIban": "UA111122223333", "currency": "UAH", "amount": 900 },
              { "id": 9, "creditorName": "Alice", "creditorIban": "UA114157523333", "currency": "UAH", "amount": 1000 },
              { "id": 10, "creditorName": "John", "creditorIban": "UA111122223333", "currency": "UAH", "amount": 1100 },
              { "id": 11, "creditorName": "Alice", "creditorIban": "UA114157523333", "currency": "UAH", "amount": 1200 }
            ]
            """;
        stubFor(get("/accounts/UA123/transactions")
            .willReturn(okJson(transactions)));
        var result = accountsService.getTransactionHistory("UA123");
        for (int i = 0; i < 10; i += 2) {
            assertEquals("John", result.get(i).creditorName());
            assertEquals("UA111122223333", result.get(i).creditorIban());
        }
        for (int i = 1; i < 10; i += 2) {
            assertEquals("Alice", result.get(i).creditorName());
            assertEquals("UA114157523333", result.get(i).creditorIban());
        }
        assertEquals(2L, result.getFirst().id());
        assertEquals(11L, result.getLast().id());
        assertEquals(10, result.size());
    }
}
