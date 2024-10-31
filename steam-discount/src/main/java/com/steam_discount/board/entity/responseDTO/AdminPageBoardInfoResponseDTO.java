package com.steam_discount.board.entity.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminPageBoardInfoResponseDTO {
    private long id;
    private String name;
    private long postCount;
}
