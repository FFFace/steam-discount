package com.steam_discount.board.entity.responseDTO;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PostPageResponseDTO {
    private Long id;
    private String writer;
    private String name;
    private Integer thumbsUp;
    private Integer thumbsDown;
    private LocalDateTime createdAt;
}
