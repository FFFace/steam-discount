package com.steam_discount.user.controller;


import com.steam_discount.common.security.jwt.user.CustomUser;
import com.steam_discount.user.entity.Login;
import com.steam_discount.user.entity.responseDTO.UserInfoResponseDTO;
import com.steam_discount.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LoginRestController {

    private final UserService userService;

    @PostMapping("/login")
    public void login(@RequestBody Login login, HttpServletResponse response){
        userService.login(login, response);
    }

    @PostMapping("/logout")
    public void logout(@AuthenticationPrincipal CustomUser customUser, HttpServletResponse response){
        userService.logout(customUser.getUser(), response);
    }

    @GetMapping("/token-check")
    public UserInfoResponseDTO tokenCheck(@AuthenticationPrincipal CustomUser customUser){
        return new UserInfoResponseDTO(customUser.getUser().getNickname(), customUser.getUser().getRole().name());
    }
}
