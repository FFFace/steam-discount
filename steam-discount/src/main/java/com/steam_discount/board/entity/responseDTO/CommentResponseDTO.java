package com.steam_discount.board.entity.responseDTO;

import com.steam_discount.board.entity.Post;
import com.steam_discount.user.entity.User;
import java.util.List;
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
    private CommentPageRespopnseDTO replyCommentPageResponseDTO;
}
