package com.steam_discount.common.security.jwt.user;

import java.util.Collection;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;


@Getter
public class CustomUser extends User {

    private com.steam_discount.user.entity.User user;

    public CustomUser(com.steam_discount.user.entity.User user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getEmail(), user.getPassword(), authorities);
        this.user = user;
    }
}
