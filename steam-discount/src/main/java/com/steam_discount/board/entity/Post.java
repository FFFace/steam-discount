package com.steam_discount.board.entity;

import com.steam_discount.board.entity.responseDTO.CommentResponseDTO;
import com.steam_discount.board.entity.responseDTO.PostPageResponseDTO;
import com.steam_discount.board.entity.responseDTO.PostResponseDTO;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "post")
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer")
    private User writer;

    @Column
    private String name;

    @Column
    private String content;

    @Column
    private int thumbsUp;

    @Column
    private int thumbsDown;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<Comment> commentList;



    public PostPageResponseDTO toPageResponseDTO(){
        PostPageResponseDTO pageResponseDTO = new PostPageResponseDTO();

        pageResponseDTO.setId(id);
        pageResponseDTO.setBoardName(board.getName());
        pageResponseDTO.setWriter(writer.getNickname());
        pageResponseDTO.setName(name);
        pageResponseDTO.setThumbsUp(thumbsUp);
        pageResponseDTO.setThumbsDown(thumbsDown);

        return pageResponseDTO;
    }

    public PostResponseDTO toPostResponseDTO(){
        PostResponseDTO postResponseDTO = new PostResponseDTO();

        postResponseDTO.setContent(content);
        postResponseDTO.setThumbsUp(thumbsUp);
        postResponseDTO.setThumbsDown(thumbsDown);
        postResponseDTO.setCreatedAt(getCreatedAt().format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm")));

        List<CommentResponseDTO> commentResponseDTOList = new ArrayList<>();
        for(Comment comment: commentList){
            commentResponseDTOList.add(comment.toResponseDTO());
        }

        postResponseDTO.setCommentList(commentResponseDTOList);

        return postResponseDTO;
    }
}
