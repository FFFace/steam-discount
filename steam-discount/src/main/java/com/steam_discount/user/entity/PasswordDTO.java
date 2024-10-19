package com.steam_discount.user.entity;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordDTO {
    @NotBlank
    private String oldPassword;
    @NotBlank
    private String newPassword;
    @NotBlank
    private String newPasswordValid;
}
