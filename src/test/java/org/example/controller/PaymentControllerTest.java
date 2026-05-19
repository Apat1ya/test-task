package org.example.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import org.example.dto.payment.PaymentRequestDto;
import org.example.dto.payment.PaymentResponseDto;
import org.example.model.payment.PaymentStatus;
import org.example.openbanking.OpenBankingClient;
import org.example.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.json.JsonMapper;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper objectMapper;

    @MockitoBean
    private PaymentService paymentService;

    @MockitoBean
    private OpenBankingClient openBankingClient;

    @Transactional
    @Test
    void initiatePayment() throws Exception {
        PaymentRequestDto request = new PaymentRequestDto(
            null,
            "UA123456789012345678901234567",
            "UAH",
            BigDecimal.valueOf(100.50),
            "Amazon",
            "UA987654321098765432109876543"
        );

        PaymentResponseDto expected = new PaymentResponseDto(
            1L,
            PaymentStatus.SUCCESS,
            request.fromIban(),
            request.currency(),
            request.amount(),
            request.creditorName(),
            request.creditorIban()
        );

        when(paymentService.initiatePayment(any(PaymentRequestDto.class)))
            .thenReturn(expected);

        MvcResult mvcResult = mockMvc.perform(post("/payments/initiate")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andReturn();

        PaymentResponseDto actual = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(),
            PaymentResponseDto.class
        );

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);

        verify(paymentService).initiatePayment(request);
    }
}
