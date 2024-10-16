package com.steam_discount.board.entity.dto;


import com.steam_discount.board.entity.Comment;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {

    @NotBlank
    private long postId;
    private Long parentId;
    @NotBlank
    private String content;


    /**
     * Entity 를 리턴합니다.<br/>
     * content, parentId, thumbsUp, thumbsDown 을 초기화 합니다.
     * @return Comment
     */
    public Comment toEntity(){
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setParentId(parentId);
        comment.setThumbsUp(0);
        comment.setThumbsDown(0);

        return comment;
    }
}
