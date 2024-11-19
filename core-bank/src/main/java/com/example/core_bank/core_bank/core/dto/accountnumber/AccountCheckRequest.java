package com.example.core_bank.core_bank.core.dto.accountnumber;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class AccountCheckRequest {
    @NotBlank(message = "계좌번호는 필수입니다.")
    private String accountNumber;

    @NotBlank(message = "예금주명은 필수입니다.")
    private String name;

    @NotBlank(message = "은행을 선택해 주세요.")
    public String bankCode;

}
