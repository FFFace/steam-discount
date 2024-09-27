package com.steam_discount.board.service;


import com.steam_discount.board.entity.Board;
import com.steam_discount.board.repository.BoardRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;



    public List<Board> findAll(){
        return boardRepository.findAll();
    }

    public Board save(Board board){
        return boardRepository.save(board);
    }
}
