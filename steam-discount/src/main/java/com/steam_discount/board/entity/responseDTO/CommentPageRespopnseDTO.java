package com.steam_discount.board.entity.responseDTO;


import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentPageRespopnseDTO {
    private List<CommentResponseDTO> commentResponseDTOList;
    private int totalPage;
}
