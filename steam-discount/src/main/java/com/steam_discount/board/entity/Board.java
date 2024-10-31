package com.steam_discount.board.entity;


import com.steam_discount.board.entity.responseDTO.AdminPageBoardInfoResponseDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "board")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String name;


    public AdminPageBoardInfoResponseDTO toBoardInfoResponseDTO(){
        AdminPageBoardInfoResponseDTO boardInfoResponseDTO = new AdminPageBoardInfoResponseDTO();
        boardInfoResponseDTO.setId(id);
        boardInfoResponseDTO.setName(name);

        return boardInfoResponseDTO;
    }
}
