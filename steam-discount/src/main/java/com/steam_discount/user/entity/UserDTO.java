package com.steam_discount.user.entity;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    private String nickname;
    private String product;
    private String productId;

    public User toEntity(){
        return new User(email, password, nickname, product, productId, UserRole.HOLD);
    }
}
