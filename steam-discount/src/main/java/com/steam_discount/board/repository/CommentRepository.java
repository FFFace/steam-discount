package com.steam_discount.board.repository;

import com.steam_discount.board.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByPostIdAndParentIdIsNullAndDisableIsNull(long postId, Pageable pageable);
    Page<Comment> findByParentId(long parentId, Pageable pageable0);
}
