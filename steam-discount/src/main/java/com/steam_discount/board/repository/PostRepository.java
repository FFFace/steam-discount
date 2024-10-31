package com.steam_discount.board.repository;

import com.steam_discount.board.entity.Post;
import com.steam_discount.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByBoardIdAndDisableIsNull(int boardId, Pageable pageable);
    Long countByBoardIdAndDisableIsNull(int boardId);
    Page<Post> findByWriterOrderByBoardIdAscIdAsc(User writer, Pageable pageable);
    Optional<Post> findFirstByBoardIdOrderByCreatedAtDesc(int boardId);
    @Query(value = "select * from post where board_id != 1 and disable is null order by created_at desc limit 10", nativeQuery = true)
    List<Post> findOrderByCreatedAtDescLimit10();
}
