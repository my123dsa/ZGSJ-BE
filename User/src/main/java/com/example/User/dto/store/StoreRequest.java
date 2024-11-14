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
    @NotBlank
    private String location;
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;



    public Store toEntity(President president) {
        return Store.createStore(
                this.getStoreName(),
                this.getBusinessNumber(),
                this.getAccountNumber(),
                this.getBankCode(),
                president,
                this.location,
                this.latitude,
                this.longitude
        );
    }
}