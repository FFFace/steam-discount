package com.steam_discount.user.controller;


import com.steam_discount.user.entity.User;
import com.steam_discount.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
}
