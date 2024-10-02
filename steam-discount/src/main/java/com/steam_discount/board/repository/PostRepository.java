package com.steam_discount.board.repository;

import com.steam_discount.board.entity.Board;
import com.steam_discount.board.entity.Post;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value = "select * from post where board_id=:boardId limit :startIndex, 10", nativeQuery = true)
    List<Post> findByBoardIdAndLimit10(long boardId, Integer startIndex);

    Page<Post> findByBoardId(long boardId, Pageable pageable);
    Optional<Post> findLastByBoardId(long boardId);
}
