package com.example.User.dto.login;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
public class ReqPwChange {

    @NotBlank(message = "현재 비밀번호 입력은 필수 입력값입니다.")
    private String beforePassword;

    @NotBlank(message = "바꿀 비밀번호를 입력하십시오.")
    private String newPassword;
}
