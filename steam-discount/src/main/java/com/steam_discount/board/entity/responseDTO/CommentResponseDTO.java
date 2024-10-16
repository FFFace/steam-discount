package com.steam_discount.board.entity.responseDTO;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CommentResponseDTO {
    private long id;
    private Long parentId;
    private String writer;
    private String content;
    private String createdAt;
    private int thumbsUp;
    private int thumbsDown;
    private CommentPageResponseDTO replyCommentPageResponseDTO;
}
