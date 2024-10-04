package com.steam_discount.board.entity;

import com.steam_discount.board.entity.responseDTO.CommentResponseDTO;
import com.steam_discount.common.entity.BaseEntity;
import com.steam_discount.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comment")
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer")
    private User writer;

    @Column
    private String content;

    @Column
    private int thumbsUp;

    @Column
    private int thumbsDown;



    public CommentResponseDTO toResponseDTO(){
        CommentResponseDTO commentResponseDTO = new CommentResponseDTO();

        commentResponseDTO.setId(id);
        commentResponseDTO.setPostId(post.getId());
        commentResponseDTO.setWriter(writer.getNickname());
        commentResponseDTO.setContent(content);
        commentResponseDTO.setThumbsUp(thumbsUp);
        commentResponseDTO.setThumbsDown(thumbsDown);

        return commentResponseDTO;
    }
}
