package com.example.bonus.service;

import com.example.bonus.dto.TransactionDTO;
import com.example.bonus.entity.Card;
import com.example.bonus.entity.Transaction;
import com.example.bonus.repository.CardRepository;
import com.example.bonus.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardService {
    private final TransactionRepository transactionRepository;
    private final CardRepository cardRepository;

    @Autowired
    public CardService(TransactionRepository transactionRepository, CardRepository cardRepository) {
        this.transactionRepository = transactionRepository;
        this.cardRepository = cardRepository;
    }

    @Transactional
    public TransactionDTO credit(String cardNumber, BigDecimal amount) {
        Card card = findCardByNumber(cardNumber);
        Transaction transaction = processTransaction(card, amount, Transaction.TransactionType.CREDIT);
        return toTransactionDTO(transaction);
    }

    @Transactional
    public TransactionDTO debit(String cardNumber, BigDecimal amount) {
        Card card = findCardByNumber(cardNumber);
        if (card.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        Transaction transaction = processTransaction(card, amount.negate(), Transaction.TransactionType.DEBIT);
        return toTransactionDTO(transaction);
    }

    @Transactional
    public TransactionDTO refund(Long transactionId) {
        Transaction originalTransaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
        BigDecimal adjustmentAmount = originalTransaction.getType() == Transaction.TransactionType.DEBIT ?
                originalTransaction.getAmount() : originalTransaction.getAmount().negate();
        Transaction transaction = processTransaction(originalTransaction.getCard(), adjustmentAmount, Transaction.TransactionType.REFUND);
        return toTransactionDTO(transaction);
    }

    public List<TransactionDTO> getTransactionsByCardNumber(String cardNumber) {
        Card card = findCardByNumber(cardNumber);
        return card.getTransactions().stream()
                .map(this::toTransactionDTO)
                .collect(Collectors.toList());
    }

    public BigDecimal getBalanceByCardNumber(String cardNumber) {
        Card card = findCardByNumber(cardNumber);
        return card.getBalance();
    }

    private Card findCardByNumber(String cardNumber) {
        return cardRepository.findByNumber(cardNumber)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));
    }

    @Transactional
    public Transaction processTransaction(Card card, BigDecimal amount, Transaction.TransactionType type) {
        Transaction transaction = new Transaction(null, type, amount.abs(), LocalDateTime.now(),
                Transaction.TransactionStatus.COMPLETED, card);
        transactionRepository.save(transaction);
        updateBalance(card, amount);
        return transaction;
    }

    private void updateBalance(Card card, BigDecimal amount) {
        card.setBalance(card.getBalance().add(amount));
        cardRepository.save(card);
    }

    private TransactionDTO toTransactionDTO(Transaction transaction) {
        return new TransactionDTO(
                transaction.getId(),
                transaction.getType().toString(),
                transaction.getAmount(),
                transaction.getTimestamp(),
                transaction.getStatus().toString());
    }
}
