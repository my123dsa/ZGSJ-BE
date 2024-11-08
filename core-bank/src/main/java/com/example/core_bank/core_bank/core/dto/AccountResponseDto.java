package com.example.core_bank.core_bank.core.dto;

import com.example.core_bank.core_bank.core.model.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor // JSON 직렬화를 위해 필요할 경우 추가
public class AccountResponseDto {

    private String accountNumber;
    private String name;
    private Integer balance;
    private String bankName;
    private String bankCode;

    /**
     * Account 엔티티를 AccountResponseDto로 변환하는 정적 메소드
     */
    public static AccountResponseDto from(Account account) {
        return new AccountResponseDto(
                account.getAccountNumber(),
                account.getName(),
                account.getBalance(),
                account.getBank().getBankName(),
                account.getBank().getBankCode()
        );
    }
}
