package com.example.User.dto.login;

import com.example.User.model.President;
import com.example.User.model.Store;
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
    private String email;
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
