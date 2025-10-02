package com.telecom.billing.telecom_billing.Models;

import java.util.ArrayList;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor 
@AllArgsConstructor
public class Plan extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private double ratePerMinute;

    @Column(nullable = false)
    private double ratePerGB;

    @Column(nullable = false)
    private double ratePerSMS;

    @OneToMany(mappedBy = "plan")
    @JsonManagedReference
    

    private List<Customer> customers = new ArrayList<>();
    
    @Override
    public String toString() {
        return "Plan{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", ratePerMinute=" + ratePerMinute +
                ", ratePerGB=" + ratePerGB +
                ", ratePerSMS=" + ratePerSMS +
                '}';
    }

}

