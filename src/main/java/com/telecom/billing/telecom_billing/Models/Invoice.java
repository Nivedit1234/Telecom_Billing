package com.telecom.billing.telecom_billing.Models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonBackReference;


/*
================================
= INVOICE ENTITY – EXPLANATION =
================================

• @Entity  
     Marks this class as a JPA entity mapped to the "invoice" table.

• Extends BaseEntity  
     Inherits createdAt and updatedAt timestamps handled by Hibernate.

• @Id + @GeneratedValue(IDENTITY)  
     Auto-increment primary key (PostgreSQL generates it).

• @ManyToOne(fetch = LAZY) customer  
     Many invoices belong to one customer.  
     Lazy loading avoids fetching customer data unless needed.

• @JoinColumn(name = "customer_id", nullable = false)  
     Adds "customer_id" foreign key column linking Invoice → Customer.

• @JsonBackReference  
     Prevents infinite JSON recursion (Customer → Invoices → Customer → ...).

• billingMonth  
     Stores which month the bill is for.  
     Always stored as the *first day* of the month (e.g., 2025-10-01).

• amount  
     Total computed charge:
         minutes * ratePerMinute
         dataGB  * ratePerGB
         sms     * ratePerSMS

• status  
     Invoice status such as: GENERATED, PAID, CANCELLED.

• generatedDate  
     Actual date when the invoice was created (LocalDate.now()).

• dueDate (optional)  
     When payment is expected.

• paymentDate (optional)  
     When payment was received.

• Note: Customer is hidden in JSON due to @JsonBackReference.
*/



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
    @JsonBackReference
    private Customer customer;

    @Column(nullable = false)
    private LocalDate billingMonth; // represents which month the bill is for (e.g. 2025-10-01)

    @Column(nullable = false)
    private double amount; // total amount to be paid

    @Column(nullable = false, length = 20)
    private String status; // e.g. GENERATED, PAID, CANCELLED

    @Column(nullable = false)
    private LocalDate generatedDate; // 🆕 when the invoice was actually created (system date)

    // Optional but realistic fields you can keep or remove:
    private LocalDate dueDate; // 🆕 when payment is expected
    private LocalDate paymentDate; // 🆕 when payment was received
}
