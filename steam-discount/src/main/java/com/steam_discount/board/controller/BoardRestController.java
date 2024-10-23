package com.steam_discount.board.controller;

import com.steam_discount.board.entity.Board;
import com.steam_discount.board.service.BoardService;
import com.steam_discount.common.security.jwt.user.CustomUser;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/{name}")
    public ResponseEntity<Board> getBoard(@PathVariable String name){
        return ResponseEntity.ok(boardService.findByName(name));
    }

    @PostMapping
    public void saveBoard(@RequestBody Map<String, String> map){
        boardService.saveBoard(map.get("boardName"));
    }
}
