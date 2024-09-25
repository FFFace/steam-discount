package com.steam_discount.user.repository;

import com.steam_discount.user.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

}
