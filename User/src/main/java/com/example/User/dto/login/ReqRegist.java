package com.example.User.dto.login;

import com.example.User.model.President;
import com.example.User.model.Store;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Setter
public class ReqRegist {
    private String name;
    private String address;

    @Email
    private String email;

    @Pattern(regexp = "^(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,}$",
            message = "비밀번호는 최소 8자 이상이며, 특수문자(!@#$%^&*)를 1개 이상 포함해야 합니다.")
    private String password;
    private LocalDate birthDate;
    private String phoneNumber;
    private Boolean termsAccept;

    public President toEntity() {
        return President.createPresident(this.name
                ,this.address
                ,this.email
                ,this.password
                ,this.birthDate, this.phoneNumber,this.termsAccept);

    }
}
