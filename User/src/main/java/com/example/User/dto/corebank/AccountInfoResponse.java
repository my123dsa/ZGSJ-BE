package com.example.User.dto.corebank;

import com.example.User.model.Store;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)  // private 생성자로 변경
public class AccountInfoResponse {
    private final String accountNumber;
    private final String bankCode;

    public static AccountInfoResponse of(String accountNumber, String bankCode) {
        return new AccountInfoResponse(accountNumber, bankCode);
    }

    public static AccountInfoResponse from(Store store) {
        return new AccountInfoResponse(
                store.getAccountNumber(),
                store.getBankCode()
        );
    }
}