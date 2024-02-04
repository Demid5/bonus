package com.example.bonus.controller;

import com.example.bonus.dto.TransactionDTO;
import com.example.bonus.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CardController.class)
class CardControllerTest {

    @MockBean
    private CardService cardService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void creditShouldReturnTransactionDTO() throws Exception {
        TransactionDTO transactionDTO = new TransactionDTO(); // Предполагается, что DTO уже определен
        given(cardService.credit("123456789", BigDecimal.valueOf(100))).willReturn(transactionDTO);

        mockMvc.perform(post("/api/credit")
                        .param("cardNumber", "123456789")
                        .param("amount", "100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void debitShouldReturnTransactionDTO() throws Exception {
        TransactionDTO transactionDTO = new TransactionDTO(); // Предполагается, что DTO уже определен
        given(cardService.debit("123456789", BigDecimal.valueOf(50))).willReturn(transactionDTO);

        mockMvc.perform(post("/api/debit")
                        .param("cardNumber", "123456789")
                        .param("amount", "50")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void refundShouldReturnTransactionDTO() throws Exception {
        TransactionDTO transactionDTO = new TransactionDTO(); // Предполагается, что DTO уже определен
        given(cardService.refund(1L)).willReturn(transactionDTO);

        mockMvc.perform(post("/api/refund")
                        .param("transactionId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getBalanceShouldReturnBigDecimal() throws Exception {
        given(cardService.getBalanceByCardNumber("123456789")).willReturn(BigDecimal.valueOf(200));

        mockMvc.perform(get("/api/balance/123456789")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("200"));
    }

    @Test
    void getTransactionsByCardNumberShouldReturnTransactionDTOList() throws Exception {
        given(cardService.getTransactionsByCardNumber("123456789")).willReturn(Collections.emptyList());

        mockMvc.perform(get("/api/transactions/123456789")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }
}
