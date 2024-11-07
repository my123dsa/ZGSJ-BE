package com.example.User.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "store")
@Getter
@NoArgsConstructor
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "store_name", nullable = false, length = 50, unique = true)
    private String storeName;

    @Column(nullable = false, length = 50)
    private String businessNumber;

    @Column(nullable = false, length = 50)
    private String accountNumber; // 사장님 계좌

    @Column(nullable = false, length = 50)
    private String bankCode;

    @OneToMany(mappedBy = "store")
    private List<StoreEmployee> storeEmployees = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "president_id", nullable = false)
    private President president;

    private Store(String storeName, String businessNumber, String accountNumber,
                  String bankCode, List<StoreEmployee> storeEmployees, President president) {
        this.storeName = storeName;
        this.businessNumber = businessNumber;
        this.accountNumber = accountNumber;
        this.bankCode = bankCode;
        this.storeEmployees = storeEmployees;
        this.president = president;
    }

    public static Store createStore(String storeName, String businessNumber, String accountNumber,
                                    String bankCode, List<StoreEmployee> storeEmployees, President president) {
        return new Store(storeName, businessNumber, accountNumber, bankCode, storeEmployees, president);
    }
}
