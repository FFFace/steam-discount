package com.steam_discount.user.entity.responseDTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserInfoResponseDTO {
    private String nickname;
    private String role;
}
