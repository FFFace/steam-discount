package com.steam_discount.board.controller;

import com.steam_discount.board.entity.Board;
import com.steam_discount.board.service.BoardService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardRestController {

    private final BoardService boardService;



    @GetMapping
    public ResponseEntity<List<Board>> getBoardList(){
        return ResponseEntity.ok(boardService.findAll());
    }
}
