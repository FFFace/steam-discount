package com.steam_discount.common.security.jwt.user;

import com.steam_discount.common.security.jwt.JwtUtil;
import com.steam_discount.user.entity.RefreshToken;
import com.steam_discount.user.entity.User;
import com.steam_discount.user.entity.UserRole;
import com.steam_discount.user.repository.RefreshTokenRepository;
import com.steam_discount.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = provider + "_" + oAuth2User.getAttribute("sub");

        Optional<User> optionalUser = userRepository.findByProductId(providerId);
        if(optionalUser.isEmpty()){
            User user = new User();
            user.setEmail(oAuth2User.getAttribute("email"));
            user.setNickname(oAuth2User.getAttribute("name"));
            user.setProduct(provider);
            user.setProductId(providerId);
            user.setRole(UserRole.USER);
            user.setVerify('T');
            user.enable();

            userRepository.save(user);
        }

        CustomUser customUser = (CustomUser) customUserDetailsService.loadUserByUsername(oAuth2User.getAttribute("email"));
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            customUser, null, List.of(new SimpleGrantedAuthority(customUser.getUser().getRole().getName())));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new CustomOAuth2User(customUser.getUser(), oAuth2User.getAttributes());
    }
}
