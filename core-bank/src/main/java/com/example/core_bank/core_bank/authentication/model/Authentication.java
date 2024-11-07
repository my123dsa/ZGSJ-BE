package com.example.core_bank.core_bank.authentication.model;

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
    private Integer id;

    @Column(name = "email", nullable = false, unique = true, length = 50)
    private String email;

    @Column(name = "pin_number", nullable = false, length = 50)
    private String pinNumber;

    private Authentication(String email, String pinNumber) {
        this.email = email;
        this.pinNumber = pinNumber;
    }

    public static Authentication createAuthentication(String email, String pinNumber) {
        return new Authentication(email, pinNumber);
    }

}
