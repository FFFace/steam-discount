package com.steam_discount.board.entity.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDTO {
    private String writer;
    private String name;
    private String content;
    private Integer thumbsUp;
    private Integer thumbsDown;
}
