package com.steam_discount.board.controller;


import com.steam_discount.board.entity.dto.PostDTO;
import com.steam_discount.board.entity.dto.PostPageDTO;
import com.steam_discount.board.entity.responseDTO.PostPageResponseDTO;
import com.steam_discount.board.entity.responseDTO.PostResponseDTO;
import com.steam_discount.board.service.PostService;
import com.steam_discount.common.security.jwt.user.CustomUser;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.jcache.interceptor.JCacheOperationSource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostRestController {

    private final PostService postService;



    @GetMapping
    public ResponseEntity<List<PostPageResponseDTO>> getPosts(@RequestBody @Valid PostPageDTO postPageDTO){
        return ResponseEntity.ok(postService.findPostList(postPageDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPost(@PathVariable Long id){
        return ResponseEntity.ok(postService.findPostById(id));
    }

    @PostMapping
    public void createPost(@RequestBody @Valid PostDTO postDTO, @AuthenticationPrincipal CustomUser customUser){
        postService.createPost(postDTO, customUser.getUser());
    }
}
