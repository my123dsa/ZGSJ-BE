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

    @Column(nullable = false, length = 200)
    private String password;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "phone_number", nullable = false, length = 50, unique = true)
    private String phoneNumber;

    @Column(name = "terms_accept", nullable = false)
    private Boolean termsAccept;

    @OneToMany(mappedBy = "president", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Store> stores = new ArrayList<>();

    private President(String name, String address, String email,String password,
                      LocalDate birthDate, String phoneNumber,
                      Boolean termsAccept) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.termsAccept = termsAccept;
        this.stores = stores;
    }

    public static President createPresident(String name, String address, String email,String password,
                                            LocalDate birthDate, String phoneNumber,
                                            Boolean termsAccept) {
        return new President(name, address, email,password, birthDate, phoneNumber, termsAccept);
    }
}
