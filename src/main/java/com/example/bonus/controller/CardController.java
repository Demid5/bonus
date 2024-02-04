package com.example.bonus.controller;

import com.example.bonus.dto.TransactionDTO;
import com.example.bonus.service.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CardController {
    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/credit")
    public ResponseEntity<TransactionDTO> credit(@RequestParam String cardNumber, @RequestParam BigDecimal amount) {
        TransactionDTO transaction = cardService.credit(cardNumber, amount);
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    @PostMapping("/debit")
    public ResponseEntity<TransactionDTO> debit(@RequestParam String cardNumber, @RequestParam BigDecimal amount) {
        TransactionDTO transaction = cardService.debit(cardNumber, amount);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @PostMapping("/refund")
    public ResponseEntity<TransactionDTO> refund(@RequestParam Long transactionId) {
        TransactionDTO transaction = cardService.refund(transactionId);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @GetMapping("/balance/{cardNumber}")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable String cardNumber) {
        BigDecimal balance = cardService.getBalanceByCardNumber(cardNumber);
        return ResponseEntity.ok(balance);
    }

    @GetMapping("/transactions/{cardNumber}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByCardNumber(@PathVariable String cardNumber) {
        List<TransactionDTO> transactions = cardService.getTransactionsByCardNumber(cardNumber);
        return ResponseEntity.ok(transactions);
    }
}
