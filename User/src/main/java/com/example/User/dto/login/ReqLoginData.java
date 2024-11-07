package com.example.User.dto.login;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@NoArgsConstructor
@Setter
public class ReqLoginData {
    private String email;
    private String password;
}
