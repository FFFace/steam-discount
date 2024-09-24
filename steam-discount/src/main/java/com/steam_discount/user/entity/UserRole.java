package com.steam_discount.user.entity;


import lombok.Getter;

@Getter
public enum UserRole {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");


    private final String name;

    UserRole(String name) {
        this.name = name;
    }
}
