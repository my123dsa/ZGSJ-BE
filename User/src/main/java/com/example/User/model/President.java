package com.example.User.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "president")
@Getter
@NoArgsConstructor
public class President {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "president_id", nullable = false)
    private Integer presidentId;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 150)
    private String address;

    @Column(name = "business_number", nullable = false, length = 50, unique = true)
    private String businessNumber;

    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "account_number", nullable = false, length = 50, unique = true)
    private String accountNumber;

    @Column(name = "phone_number", nullable = false, length = 50, unique = true)
    private String phoneNumber;

    @Column(name = "temrs_accept", nullable = false)
    private Boolean temrsAccept;

    @OneToMany(mappedBy = "president")
    private List<Employee> employees = new ArrayList<>();

    public static President createPresident(String name, String address, String businessNumber, String email, LocalDate birthDate, String accountNumber, String phoneNumber, Boolean temrsAccept) {
        President president = new President();
        president.name = name;
        president.address = address;
        president.businessNumber = businessNumber;
        president.email = email;
        president.birthDate = birthDate;
        president.accountNumber = accountNumber;
        president.phoneNumber = phoneNumber;
        president.temrsAccept = temrsAccept;
        return president;
    }
}
