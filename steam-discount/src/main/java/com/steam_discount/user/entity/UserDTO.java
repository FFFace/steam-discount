package com.steam_discount.user.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO {
    private String email;
    private String password;
    private String nickname;
    private String product;
    private String productId;

    public User toEntity(){
        return new User(email, password, nickname, product, productId);
    }
}
