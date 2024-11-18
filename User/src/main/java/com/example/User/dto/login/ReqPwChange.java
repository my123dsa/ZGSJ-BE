package com.example.User.dto.login;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "^(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,}$",
            message = "비밀번호는 최소 8자 이상이며, 특수문자(!@#$%^&*)를 1개 이상 포함해야 합니다.")
    private String newPassword;
}
