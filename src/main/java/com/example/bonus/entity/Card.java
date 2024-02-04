package com.example.bonus.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@Table(name = "cards", indexes = {
        @Index(name = "idx_card_number", columnList = "number", unique = true)
})
public class Card implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String clientName;

    @Column(nullable = false, unique = true)
    private String number;

    @Column(nullable = false)
    private BigDecimal balance;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "card")
    private Set<Transaction> transactions;
}

