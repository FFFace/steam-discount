package com.steam_discount.board.entity.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
public class PostPageDTO {

    @NotNull(message = "게시판을 입력해 주세요.")
    private long boardId;

    @Min(0)
    private int page;
}
