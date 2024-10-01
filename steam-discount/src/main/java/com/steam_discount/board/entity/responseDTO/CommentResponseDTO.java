package com.steam_discount.board.entity.responseDTO;

import com.steam_discount.board.entity.Post;
import com.steam_discount.user.entity.User;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CommentResponseDTO {
    private long id;
    private long postId;
    private String writer;
    private String content;
    private Integer thumbsUp;
    private Integer thumbsDown;
}
