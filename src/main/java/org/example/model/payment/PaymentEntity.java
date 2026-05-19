package org.example.model.payment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String fromIban;
    @Column(nullable = false)
    private String currency;
    @Column(nullable = false)
    private BigDecimal amount;
    @Column(nullable = false)
    private String creditorName;
    @Column(nullable = false)
    private String creditorIban;
}
