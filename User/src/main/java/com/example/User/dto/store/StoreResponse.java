package com.example.User.dto.store;

import com.example.User.model.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class StoreResponse {
    private Integer id;
    private String storeName;

    public static StoreResponse from(Store store) {
        return new StoreResponse(
                store.getId(),
                store.getStoreName()
        );
    }
}
