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
    @Column(name = "president_id")
    private Integer id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 150)
    private String address;

    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "phone_number", nullable = false, length = 50, unique = true)
    private String phoneNumber;

    @Column(name = "terms_accept", nullable = false)
    private Boolean termsAccept;

    @OneToMany(mappedBy = "president")
    private List<Store> stores = new ArrayList<>();

    private President(String name, String address, String email,
                      LocalDate birthDate, String phoneNumber,
                      Boolean termsAccept, List<Store> stores) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.termsAccept = termsAccept;
        this.stores = stores;
    }

    public static President createPresident(String name, String address, String email,
                                            LocalDate birthDate, String phoneNumber,
                                            Boolean termsAccept, List<Store> stores) {
        return new President(name, address, email, birthDate, phoneNumber, termsAccept, stores);
    }
}
