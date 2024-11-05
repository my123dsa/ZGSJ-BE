package com.example.core_bank.core_bank.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "authentication")
@Getter
@NoArgsConstructor
public class Authentication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authentication_id")
    private int id;

    @Column(name = "email", nullable = false, unique = true, length = 50)
    private String email;

    @Column(name = "pin_number", nullable = false, length = 50)
    private String pinNumber;

    @Column(name = "secret_key", nullable = false, length = 150)
    private String secretKey;
}
