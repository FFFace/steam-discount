package com.steam_discount.board.entity.responseDTO;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostPageListResponseDTO {
    private List<PostPageResponseDTO> postPageResponseDTOList;
    private int totalPage;
}
