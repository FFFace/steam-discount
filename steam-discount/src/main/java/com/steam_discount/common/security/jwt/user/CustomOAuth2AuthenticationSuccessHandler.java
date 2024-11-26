package com.steam_discount.common.security.jwt.user;

import com.steam_discount.common.exception.CustomException;
import com.steam_discount.common.exception.errorCode.ErrorCode;
import com.steam_discount.common.security.jwt.JwtUtil;
import com.steam_discount.user.entity.RefreshToken;
import com.steam_discount.user.repository.RefreshTokenRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomOAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null && auth.getPrincipal() instanceof CustomUser customUser){
            String refreshToken = jwtUtil.generateRefreshToken(customUser.getUser().getEmail());
            String accessToken = jwtUtil.generateAccessToken(customUser.getUser().getEmail());

            RefreshToken dbRefreshToken = new RefreshToken(refreshToken, customUser.getUser().getEmail());
            refreshTokenRepository.save(dbRefreshToken);

            Cookie cookie = new Cookie(jwtUtil.getREFRESH_TOKEN_COOKIE_NAME(), refreshToken);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(jwtUtil.getRefreshCookieMaxAge());
            cookie.setSecure(true);

            response.addCookie(cookie);
            response.setHeader(jwtUtil.getACCESS_TOKEN_HEADER_NAME(), accessToken);

            response.sendRedirect("/");
        } else{
            throw new IllegalStateException("Authentication 정보가 존재하지 않습니다.");
        }
    }
}
