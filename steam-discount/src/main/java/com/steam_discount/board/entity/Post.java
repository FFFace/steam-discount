package com.steam_discount.board.entity;

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
    private Long id;

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
    private Integer thumbsUp;

    @Column
    private Integer thumbsDown;

    @OneToMany(mappedBy = "post")
    private List<Comment> commentList;



    public PostResponseDTO toResponseDTO(){
        PostResponseDTO responseDTO = new PostResponseDTO();

        responseDTO.setWriter(writer.getNickname());
        responseDTO.setName(name);
        responseDTO.setContent(content);
        responseDTO.setThumbsUp(thumbsUp);
        responseDTO.setThumbsDown(thumbsDown);

        return responseDTO;
    }
}
