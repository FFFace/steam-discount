package com.steam_discount.user.entity;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Login {
    @NotBlank(message = "이메일이 옳바르지 않습니다.")
    @Email(message = "이메일이 옳바르지 않습니다.")
    private String email;
    @NotBlank(message = "패스워드가 옳바르지 않습니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,}", message = "패스워드가 옳바르지 않습니다.")
    private String password;
}
