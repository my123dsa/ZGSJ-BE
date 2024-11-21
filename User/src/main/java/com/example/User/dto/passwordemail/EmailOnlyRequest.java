package com.example.User.dto.passwordemail;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class EmailOnlyRequest {
    @Email
    @NotEmpty(message = "이메일을 입력해 주세요")
    private String email;
}
