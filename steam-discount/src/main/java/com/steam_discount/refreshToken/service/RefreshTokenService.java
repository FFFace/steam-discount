package com.steam_discount.refreshToken.service;

import com.steam_discount.common.exception.CustomException;
import com.steam_discount.common.exception.errorCode.ErrorCode;
import com.steam_discount.user.entity.RefreshToken;
import com.steam_discount.user.repository.RefreshTokenRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public void deleteRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.delete(refreshToken);
    }

    public RefreshToken findByEmail(String email) {
        return refreshTokenRepository.findByEmail(email).orElseThrow(() ->
            new CustomException(ErrorCode.NOT_FOUND_REFRESH_TOKEN));
    }

    public RefreshToken findByEmailOrNull(String email) {
        return refreshTokenRepository.findByEmail(email).orElse(null);
    }

    public Optional<RefreshToken> findByEmailOptional(String email){
        return refreshTokenRepository.findByEmail(email);
    }

    public void saveRefreshToken(String token, String email){
        RefreshToken refreshToken = refreshTokenRepository.findByEmail(email).orElse(new RefreshToken());
        refreshToken.setEmail(email);
        refreshToken.setToken(token);

        refreshTokenRepository.save(refreshToken);
    }

    public void saveRefreshToken(RefreshToken refreshToken){
        refreshTokenRepository.save(refreshToken);
    }
}
