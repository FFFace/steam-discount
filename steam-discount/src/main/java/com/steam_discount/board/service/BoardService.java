package com.steam_discount.board.service;


import com.steam_discount.board.entity.Board;
import com.steam_discount.board.repository.BoardRepository;
import com.steam_discount.common.exception.CustomException;
import com.steam_discount.common.exception.errorCode.ErrorCode;
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



    public List<Board> findAll(){
        return boardRepository.findAll(Sort.by(Direction.ASC, "id"));
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
