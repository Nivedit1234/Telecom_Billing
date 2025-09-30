package com.telecom.billing.telecom_billing.Models;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Invoice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(nullable = false)
    private LocalDate billingMonth;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false, length = 20)
    private String status; // e.g. GENERATED, PAID, CANCELLED
}
