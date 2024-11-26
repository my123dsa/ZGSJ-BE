package com.example.User.model;

import com.example.User.dto.store.StoreRequest;
import com.example.User.dto.store.StoreUpdateRequest;
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
    @Column(name = "store_id")
    private Integer id;

    @Column(name = "store_name", nullable = false, length = 50, unique = true)
    private String storeName;

    @Column(nullable = false, length = 50)
    private String businessNumber;

    @Column(nullable = false, length = 50)
    private String accountNumber; // 사장님 계좌

    @Column(nullable = false, length = 50)
    private String bankCode;

    @Column(name = "location",length = 100, nullable = false)
    private String location;
    @Column(name = "latitude", nullable = false)
    private Double latitude;
    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StoreEmployee> storeEmployees = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "president_id", nullable = false)
    private President president;

    private Store(String storeName, String businessNumber, String accountNumber,
                  String bankCode, President president,String location,Double latitude,Double longitude) {
        this.storeName = storeName;
        this.businessNumber = businessNumber;
        this.accountNumber = accountNumber;
        this.bankCode = bankCode;
        this.president = president;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static Store createStore(String storeName, String businessNumber, String accountNumber,
                                    String bankCode, President president,String location,Double latitude,Double longitude) {
        return new Store(storeName, businessNumber, accountNumber, bankCode, president,location,latitude,longitude);
    }

    public void updateByStoreRequest(StoreUpdateRequest request) {
        this.storeName = request.getStoreName();
        this.accountNumber = request.getAccountNumber();
        this.location= request.getLocation();
        this.latitude = request.getLatitude();
        this.longitude = request.getLongitude();

    }
}
