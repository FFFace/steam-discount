package com.steam_discount.common.security.jwt;

import com.steam_discount.common.security.jwt.user.CustomUser;
import com.steam_discount.common.security.jwt.user.CustomUserDetailsService;
import com.steam_discount.user.entity.RefreshToken;
import com.steam_discount.user.repository.RefreshTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        String accessToken = request.getHeader("Authorization");


        if (accessToken == null || accessToken.equals("undefined")) {
            validateRefreshToken(request, response);
        }

        validateAccessToken(accessToken, request, response);

        filterChain.doFilter(request, response);
    }

    private void validateRefreshToken(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();

        if(cookies == null){
            return;
        }

        Cookie cookie = Arrays.stream(cookies).filter(c -> c.getName().equals("refreshToken")).findFirst().orElse(null);

        if(cookie == null){
            return;
        }

        if(!jwtUtil.validateRefreshToken(cookie.getValue())){
            return;
        }

        validateUserAndTokensRefresh(cookie, response);
    }

    private void validateUserAndTokensRefresh(Cookie cookie, HttpServletResponse response){
        String email = jwtUtil.getEmailFromRefreshToken(cookie.getValue());

        String refreshToken = cookie.getValue();
        RefreshToken dbRefreshToken = refreshTokenRepository.findByEmail(email).orElse(null);

        if(dbRefreshToken == null){
            return;
        } else if(!dbRefreshToken.getToken().equals(refreshToken)){
            refreshTokenRepository.delete(dbRefreshToken);
            return;
        }

        String accessToken = jwtUtil.generateAccessToken(email);

        String newRefreshToken = jwtUtil.generateRefreshToken(email);

        dbRefreshToken.setToken(newRefreshToken);
        refreshTokenRepository.save(dbRefreshToken);

        Cookie newCookie = new Cookie(jwtUtil.getREFRESH_TOKEN_COOKIE_NAME(), newRefreshToken);
        newCookie.setHttpOnly(true);
        newCookie.setPath("/");
        newCookie.setMaxAge(jwtUtil.getRefreshCookieMaxAge());
        newCookie.setSecure(true);

        response.addCookie(newCookie);

        validateUser(accessToken);
    }

    private void validateAccessToken(String accessToken, HttpServletRequest request,  HttpServletResponse response){
        if(!jwtUtil.validateAccessToken(accessToken)){
            validateRefreshToken(request, response);
        } else{
            validateUser(accessToken);
        }
    }

    private void validateUser(String accessToken){
        String email = jwtUtil.getEmailFromAccessToken(accessToken);

        CustomUser user = (CustomUser) customUserDetailsService.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            user, null, List.of(new SimpleGrantedAuthority(user.getUser().getRole().getName())));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
