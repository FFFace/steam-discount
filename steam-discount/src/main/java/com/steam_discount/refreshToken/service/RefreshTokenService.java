package com.steam_discount.refreshToken.service;

import com.steam_discount.common.exception.CustomException;
import com.steam_discount.common.exception.errorCode.ErrorCode;
import com.steam_discount.user.entity.RefreshToken;
import com.steam_discount.user.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void deleteRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.delete(refreshToken);
    }

    @Transactional
    public RefreshToken findByEmail(String email) {
        return refreshTokenRepository.findByEmail(email).orElseThrow(() ->
            new CustomException(ErrorCode.NOT_FOUND_REFRESH_TOKEN));
    }

    @Transactional
    public RefreshToken findByEmailOrNull(String email) {
        return refreshTokenRepository.findByEmail(email).orElse(null);
    }

    @Transactional
    public Optional<RefreshToken> findByEmailOptional(String email){
        return refreshTokenRepository.findByEmail(email);
    }

    @Transactional
    public void saveRefreshToken(String token, String email){
        RefreshToken refreshToken = refreshTokenRepository.findByEmail(email).orElse(new RefreshToken());
        refreshToken.setEmail(email);
        refreshToken.setToken(token);

        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void saveRefreshToken(RefreshToken refreshToken){
        refreshTokenRepository.save(refreshToken);
    }
}
