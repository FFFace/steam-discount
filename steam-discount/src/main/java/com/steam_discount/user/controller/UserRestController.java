package com.steam_discount.user.controller;


import com.steam_discount.common.security.jwt.user.CustomUser;
import com.steam_discount.user.entity.Login;
import com.steam_discount.user.entity.User;
import com.steam_discount.user.entity.UserDTO;
import com.steam_discount.user.entity.VerifyEmail;
import com.steam_discount.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;
    @GetMapping
    public ResponseEntity<List<User>> getUserList(){
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @GetMapping("/email")
    public ResponseEntity<String> getUserEmail(@AuthenticationPrincipal CustomUser customUser){
        return ResponseEntity.ok(customUser.getUser().getEmail());
    }

    @PostMapping
    public void createUser(@RequestBody @Valid UserDTO userDTO){
        userService.saveNewUser(userDTO);
    }

    @PostMapping("/verify")
    public ResponseEntity<Boolean> verifyUserEmail(@RequestBody VerifyEmail verify){
        return ResponseEntity.ok(userService.verifiedCode(verify.getEmail(), verify.getCode()));
    }

    @PostMapping("/duplicate-email")
    public ResponseEntity<Boolean> duplicateUserEmail(@RequestBody Map<String, String> map){
        return ResponseEntity.ok(userService.duplicateUserEmail(map.get("email")));
    }

    @PostMapping("/duplicate-nickname")
    public ResponseEntity<Boolean> duplicateUserNickname(@RequestBody Map<String, String> map){
        return ResponseEntity.ok(userService.duplicateUserNickname(map.get("nickname")));
    }

    @PatchMapping("/nickname")
    public void updateUserNickname(@RequestBody Map<String, String> map, @AuthenticationPrincipal CustomUser customUser){
        userService.updateNickname(map.get("nickname"), customUser.getUser());
    }
}
