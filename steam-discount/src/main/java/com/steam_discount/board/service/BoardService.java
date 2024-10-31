package com.steam_discount.board.service;


import com.steam_discount.board.entity.Board;
import com.steam_discount.board.entity.responseDTO.AdminPageBoardInfoResponseDTO;
import com.steam_discount.board.repository.BoardRepository;
import com.steam_discount.board.repository.PostRepository;
import com.steam_discount.common.exception.CustomException;
import com.steam_discount.common.exception.errorCode.ErrorCode;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final PostRepository postRepository;


    public List<AdminPageBoardInfoResponseDTO> findAll(){
        List<Board> boardList = boardRepository.findAll(Sort.by(Direction.ASC, "id"));
        List<AdminPageBoardInfoResponseDTO> boardInfoResponseDTOList = new ArrayList<>();

        boardList.forEach(board -> {
            AdminPageBoardInfoResponseDTO boardInfoResponseDTO = board.toBoardInfoResponseDTO();
            long postCount = postRepository.countByBoardIdAndDisableIsNull(board.getId());
            boardInfoResponseDTO.setPostCount(postCount);

            boardInfoResponseDTOList.add(boardInfoResponseDTO);
        });

        return boardInfoResponseDTOList;
    }

    public Board findByName(String name){
        return boardRepository.findByName(name).orElseThrow(() ->
            new CustomException(ErrorCode.NOT_FOUND_BOARD));
    }

    public void saveBoard(String boardName){
        if(boardRepository.findByName(boardName).isPresent()){
            throw new CustomException(ErrorCode.ALREADY_EXIST_BOARD);
        }

        Board board = new Board();
        board.setName(boardName);

        boardRepository.save(board);
    }
}
