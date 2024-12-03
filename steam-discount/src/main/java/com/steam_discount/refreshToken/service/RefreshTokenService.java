package com.steam_discount.refreshToken.service;

import com.steam_discount.common.exception.CustomException;
import com.steam_discount.common.exception.errorCode.ErrorCode;
import com.steam_discount.user.entity.RefreshToken;
import com.steam_discount.user.repository.RefreshTokenRepository;
import com.steam_discount.user.repository.UserRepository;
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

@Service
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Transactional
    @Retryable(value = {ObjectOptimisticLockingFailureException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void deleteRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.delete(refreshToken);
    }

    @Recover
    public void recover(ObjectOptimisticLockingFailureException e) {
        log.error("RefreshTokenService Retry failed after 3 attempts", e);
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
        if (!userRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }

        Optional<RefreshToken> existingToken = refreshTokenRepository.findByEmail(email);
        existingToken.ifPresent(this::deleteRefreshToken);

        RefreshToken refreshToken = refreshTokenRepository.findByEmail(email).orElse(new RefreshToken());
        refreshToken.setEmail(email);
        refreshToken.setToken(token);

        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void saveRefreshToken(RefreshToken refreshToken){
        if (!userRepository.existsByEmail(refreshToken.getEmail())) {
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }

        refreshTokenRepository.save(refreshToken);
    }
}
