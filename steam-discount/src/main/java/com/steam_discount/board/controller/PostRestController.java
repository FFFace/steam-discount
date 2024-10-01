package com.steam_discount.board.controller;


import com.steam_discount.board.entity.Post;
import com.steam_discount.board.entity.dto.PostDTO;
import com.steam_discount.board.entity.dto.PostPageDTO;
import com.steam_discount.board.entity.responseDTO.PostResponseDTO;
import com.steam_discount.board.service.PostService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostRestController {

    private final PostService postService;



    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> getPosts(@RequestBody @Valid PostPageDTO postPageDTO){
        return ResponseEntity.ok(postService.findPostList(postPageDTO));
    }
}
