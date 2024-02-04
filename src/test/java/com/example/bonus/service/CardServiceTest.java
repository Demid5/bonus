package com.example.bonus.service;

import com.example.bonus.dto.TransactionDTO;
import com.example.bonus.entity.Card;
import com.example.bonus.entity.Transaction;
import com.example.bonus.repository.CardRepository;
import com.example.bonus.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardService cardService;

    private Card card;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        // Инициализация тестовых данных
        card = new Card();
        card.setId(1L);
        card.setNumber("123456789");
        card.setBalance(BigDecimal.valueOf(1000));

        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setCard(card);
        transaction.setAmount(BigDecimal.valueOf(100));
        transaction.setType(Transaction.TransactionType.CREDIT);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setStatus(Transaction.TransactionStatus.COMPLETED);
    }

    @Test
    void creditShouldCreateTransactionAndIncreaseBalance() {
        when(cardRepository.findByNumber(card.getNumber())).thenReturn(Optional.of(card));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        TransactionDTO result = cardService.credit(card.getNumber(), BigDecimal.valueOf(100));

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(100), result.getAmount());
        verify(cardRepository).findByNumber(card.getNumber());
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void debitShouldCreateTransactionAndDecreaseBalance() throws Exception {
        when(cardRepository.findByNumber(card.getNumber())).thenReturn(Optional.of(card));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        TransactionDTO result = cardService.debit(card.getNumber(), BigDecimal.valueOf(50));

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(50), result.getAmount());
        verify(cardRepository).findByNumber(card.getNumber());
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void refundShouldCreateTransactionAndAdjustBalance() throws Exception {
        Transaction refundTransaction = new Transaction();
        refundTransaction.setId(2L);
        refundTransaction.setCard(card);
        refundTransaction.setAmount(BigDecimal.valueOf(100));
        refundTransaction.setType(Transaction.TransactionType.REFUND);
        refundTransaction.setTimestamp(LocalDateTime.now());
        refundTransaction.setStatus(Transaction.TransactionStatus.COMPLETED);

        when(transactionRepository.findById(transaction.getId())).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(refundTransaction);

        TransactionDTO result = cardService.refund(transaction.getId());

        assertNotNull(result);
        verify(transactionRepository).findById(transaction.getId());
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void getTransactionsByCardNumberShouldReturnTransactionsList() {
        when(cardRepository.findByNumber(card.getNumber())).thenReturn(Optional.of(card));
        card.setTransactions(Collections.singleton(transaction));

        List<TransactionDTO> result = cardService.getTransactionsByCardNumber(card.getNumber());

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(cardRepository).findByNumber(card.getNumber());
    }

    @Test
    void getBalanceByCardNumberShouldReturnBalance() {
        when(cardRepository.findByNumber(card.getNumber())).thenReturn(Optional.of(card));

        BigDecimal result = cardService.getBalanceByCardNumber(card.getNumber());

        assertEquals(card.getBalance(), result);
        verify(cardRepository).findByNumber(card.getNumber());
    }
}
