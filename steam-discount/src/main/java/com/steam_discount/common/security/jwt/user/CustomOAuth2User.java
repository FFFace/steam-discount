package com.steam_discount.common.security.jwt.user;

import com.steam_discount.user.entity.User;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
@AllArgsConstructor
public class CustomOAuth2User implements OAuth2User {
    private User user;
    private Map<String, Object> attributes;

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getName() {
        return user.getNickname();
    }
}
