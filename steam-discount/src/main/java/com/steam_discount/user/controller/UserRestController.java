package com.steam_discount.user.controller;


import com.steam_discount.user.entity.User;
import com.steam_discount.user.entity.UserDTO;
import com.steam_discount.user.entity.VerifyEmail;
import com.steam_discount.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserRestController {

    private final UserService userService;
    @GetMapping
    public ResponseEntity<List<User>> getUserList(){
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@RequestParam Long id){
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @PostMapping
    public void createUser(@RequestBody UserDTO userDTO){
        userService.saveNewUser(userDTO);
    }

    @PostMapping("/verify")
    public ResponseEntity<Boolean> verifyUserEmail(@RequestBody VerifyEmail verify){
        return ResponseEntity.ok(userService.verifiedCode(verify.getEmail(), verify.getCode()));
    }
}
