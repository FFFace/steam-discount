package com.steam_discount.common.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.steam_discount.common.exception.ResponseException;
import com.steam_discount.common.exception.errorCode.ErrorCode;
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
            if(!validateRefreshToken(request, response)){
                filterChain.doFilter(request, response);
                return;
            }
        }

        if(!validateAccessToken(accessToken, request, response)){
            filterChain.doFilter(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    public void jwtExceptionHandler(HttpServletResponse response, ErrorCode errorCode) {
        response.setStatus(errorCode.getErrorCode());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            String json = new ObjectMapper().writeValueAsString(new ResponseException(errorCode));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private boolean validateRefreshToken(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();

        if(cookies == null){
            return false;
        }

        Cookie cookie = Arrays.stream(cookies).filter(c -> c.getName().equals("refreshToken")).findFirst().orElse(null);

        if(cookie == null){
            return false;
        }

        if(!jwtUtil.validateRefreshToken(cookie.getValue())){
            return false;
        }

        return verifyRefreshTokenToken(cookie, response);
    }

    private boolean verifyRefreshTokenToken(Cookie cookie, HttpServletResponse response){
        String email = jwtUtil.getEmailFromRefreshToken(cookie.getValue());

        String refreshToken = cookie.getValue();
        RefreshToken dbRefreshToken = refreshTokenRepository.findByEmail(email).orElse(null);

        if(dbRefreshToken == null){
            return false;
        } else if(!dbRefreshToken.getToken().equals(refreshToken)){
            refreshTokenRepository.delete(dbRefreshToken);
            return false;
        }

        String accessToken = jwtUtil.generateAccessToken(email);

        verifyAccessToken(accessToken);

        return true;
    }

    private boolean validateAccessToken(String accessToken, HttpServletRequest request,  HttpServletResponse response){
        if(!jwtUtil.validateAccessToken(accessToken)){
            return validateRefreshToken(request, response);
        } else{
            verifyAccessToken(accessToken);

            return true;
        }
    }

    private void verifyAccessToken(String accessToken){
        String email = jwtUtil.getEmailFromAccessToken(accessToken);

        CustomUser user = (CustomUser) customUserDetailsService.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            user, null, List.of(new SimpleGrantedAuthority(user.getUser().getRole().getName())));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
