package com.telecom.billing.telecom_billing.Models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(unique = true, nullable = false, length = 150)
    private String email;

    @Column(unique = true, nullable = false, length = 20)
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @OneToMany(mappedBy = "customer")
    @JsonManagedReference
    

    private List<Usage> usages = new ArrayList<>();

    @OneToMany(mappedBy = "customer")
    @JsonManagedReference
    private List<Invoice> invoices = new ArrayList<>();
}
