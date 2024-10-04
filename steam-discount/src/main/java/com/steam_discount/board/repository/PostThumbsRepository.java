package com.steam_discount.board.repository;

import com.steam_discount.board.entity.PostThumbs;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostThumbsRepository extends JpaRepository<PostThumbs, Long> {
    Optional<PostThumbs> findByPostIdAndUserId(long postId, int userId);
}
