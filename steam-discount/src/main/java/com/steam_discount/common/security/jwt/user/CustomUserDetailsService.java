package com.steam_discount.common.security.jwt.user;

import com.steam_discount.common.exception.CustomException;
import com.steam_discount.common.exception.errorCode.ErrorCode;
import com.steam_discount.user.entity.User;
import com.steam_discount.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
            new CustomException(ErrorCode.NOT_FOUND_USER));

        if(user.getDisable() == 'T'){
            throw new CustomException(ErrorCode.NO_HAVE_AUTHORITY);
        }

        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(() -> user.getRole().getName());

        return new CustomUser(user, collection);
    }
}
