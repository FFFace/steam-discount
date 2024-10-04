package com.steam_discount.board.entity.dto;


import com.steam_discount.board.entity.Comment;
import com.steam_discount.board.entity.Post;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.checker.units.qual.N;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {

    private int boardId;
    private String name;
    private String content;

    public Post toEntity(){
        Post post = new Post();
        post.setName(name);
        post.setContent(content);
        post.setThumbsUp(0);
        post.setThumbsDown(0);

        return post;
    }
}
