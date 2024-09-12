package com.steam_discount.user.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VerifyEmail {
    private String email;
    private String code;
}
