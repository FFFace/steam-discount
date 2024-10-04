package com.steam_discount.board.entity.responseDTO;

import com.steam_discount.board.entity.Comment;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDTO {
    private String content;
    private int thumbsUp;
    private int thumbsDown;
    private List<CommentResponseDTO> commentList;
}
