package com.example.User.model;

import jakarta.persistence.*;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employee")
@Getter
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private Boolean sex;

    @Column(nullable = false, length = 150)
    private String address;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "phone_number", nullable = false, length = 50, unique = true)
    private String phoneNumber;

    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @OneToMany(mappedBy = "employee")
    private List<StoreEmployee> storeEmployees = new ArrayList<>();

    private Employee(String name, Boolean sex, String address, LocalDate birthDate,
                    String phoneNumber, String email, List<StoreEmployee> storeEmployees) {
        this.name = name;
        this.sex = sex;
        this.address = address;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.storeEmployees = storeEmployees;
    }

    public static Employee createEmployee(String name, Boolean sex, String address, LocalDate birthDate,
                              String phoneNumber, String email, List<StoreEmployee> storeEmployees) {
        return new Employee(name, sex, address, birthDate, phoneNumber, email, storeEmployees);
    }
}
