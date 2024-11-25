package com.example.User.dto.store;

import com.example.User.model.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StoreResponse {
    private Integer id;
    private String storeName;
    private String businessNumber;
    private String accountNumber;
    private String bankCode;
    private String location;

    public static StoreResponse from(Store store) {
        return new StoreResponse(
                store.getId(),
                store.getStoreName(),
                store.getBusinessNumber(),
                store.getAccountNumber(),
                store.getBankCode(),
                store.getLocation()
        );
    }
}
