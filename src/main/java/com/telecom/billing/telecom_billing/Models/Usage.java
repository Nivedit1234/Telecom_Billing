package com.telecom.billing.telecom_billing.Models;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter 
@Setter
@NoArgsConstructor 
@AllArgsConstructor
public class Usage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(nullable = false)
    private LocalDate usageDate;

    @Column(nullable = false)
    private int minutesUsed;

    @Column(nullable = false)
    private double dataUsedGB;

    @Column(nullable = false)
    private int smsSent;
}
