package com.steam_discount.user.entity.responseDTO;


import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserInfoResponseDTO {
    private String email;
    private String nickname;
    private String role;
    private String createdAt;
    private Character disable;
}
