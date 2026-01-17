package com.telecom.billing.telecom_billing.Models;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Getter 
@Setter
@NoArgsConstructor 
@AllArgsConstructor
public class Usage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // • Primary key for the Usage table
    // • Auto-incremented by the database

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonBackReference

    private Customer customer;
    
    // • Many usage records belong to one customer
    // • 'customer_id' is the foreign key in the database
    // • LAZY loading improves performance
    // • JsonBackReference prevents infinite JSON recursion


    @Column(nullable = false)
    private LocalDate usageDate;
    
 // • Date on which the usage occurred
    // • Cannot be null

    @Column(nullable = false)
    private int minutesUsed;
    
 // • Total call minutes consumed on this date

    @Column(nullable = false)
    private double dataUsedGB;
    
 // • Mobile data consumed in GB for this usage record

    @Column(nullable = false)
    private int smsSent;
    
    // • Number of SMS sent on this date
}

