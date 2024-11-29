package com.example.User.dto.manager;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ManagerResponse {

    private Integer presidentid;

    private String name;

    private String email;

    private String phoneNumber;
}
