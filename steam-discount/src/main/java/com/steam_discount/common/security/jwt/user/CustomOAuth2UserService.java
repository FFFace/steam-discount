package com.steam_discount.common.security.jwt.user;

import com.steam_discount.user.entity.User;
import com.steam_discount.user.entity.UserRole;
import com.steam_discount.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
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

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = provider + "_" + oAuth2User.getAttribute("sub");

        Optional<User> optionalUser = userRepository.findByProductId(providerId);
        User user = new User();
        if(optionalUser.isEmpty()){
            user.setEmail(oAuth2User.getAttribute("email"));
            user.setNickname(oAuth2User.getAttribute("name"));
            user.setProduct(provider);
            user.setProductId(providerId);
            user.setRole(UserRole.USER);
            user.setVerify('T');
            user.enable();

            userRepository.save(user);
        } else{
            user = optionalUser.get();
        }

        return new CustomOAuth2User(user, oAuth2User.getAttributes());
    }
}
