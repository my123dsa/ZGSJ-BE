package com.example.core_bank.core_bank.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "business_number")
@Getter
@NoArgsConstructor
public class BusinessNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bs_id")
    private Integer id;

    @Column(name = "business_number", nullable = false , length = 50)
    private String businessNumber;

    private BusinessNumber(String businessNumber) {
        this.businessNumber = businessNumber;
    }

    public static BusinessNumber createBusinessNumber(String businessNumber) {
        return new BusinessNumber(businessNumber);
    }
}
