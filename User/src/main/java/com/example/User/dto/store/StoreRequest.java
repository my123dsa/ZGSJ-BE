package com.example.User.dto.store;

import com.example.User.model.President;
import com.example.User.model.Store;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class StoreRequest {

    @NotNull
    private Integer id;

    @NotBlank
    private String storeName;

    @NotBlank
    private String businessNumber;

    @NotBlank
    private String accountNumber;

    @NotBlank
    private String bankCode;

    public Store toEntity(Integer storeId, President president) {
        return Store.createStore(
                storeId,
                this.getStoreName(),
                this.getBusinessNumber(),
                this.getAccountNumber(),
                this.getBankCode(),
                president
        );
    }
}