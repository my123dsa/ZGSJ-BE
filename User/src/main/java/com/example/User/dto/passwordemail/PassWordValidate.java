package com.example.User.dto.passwordemail;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PassWordValidate {

    @Pattern(regexp = "^(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,}$",
            message = "비밀번호는 최소 8자 이상이며, 특수문자(!@#$%^&*)를 1개 이상 포함해야 합니다.")
    private String password;
}
