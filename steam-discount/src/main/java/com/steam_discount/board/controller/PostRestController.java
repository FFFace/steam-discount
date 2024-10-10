package com.steam_discount.board.controller;


import com.steam_discount.board.entity.dto.CommentDTO;
import com.steam_discount.board.entity.dto.PostDTO;
import com.steam_discount.board.entity.responseDTO.PostPageListResponseDTO;
import com.steam_discount.board.entity.responseDTO.PostPageResponseDTO;
import com.steam_discount.board.entity.responseDTO.PostResponseDTO;
import com.steam_discount.board.entity.responseDTO.PostThumbsResponseDTO;
import com.steam_discount.board.service.PostService;
import com.steam_discount.common.security.jwt.user.CustomUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/posts")
public class PostRestController {

    private final PostService postService;



    @GetMapping
    public ResponseEntity<PostPageListResponseDTO> getPosts(@RequestParam int boardId, @RequestParam(required = false, defaultValue = "0") int page){
        return ResponseEntity.ok(postService.findPostListByBoardIdResponse(boardId, page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPost(@PathVariable long id, @AuthenticationPrincipal CustomUser customUser){
        return ResponseEntity.ok(postService.findPostByIdResponse(id, customUser));
    }

    @GetMapping("/main-notice")
    public ResponseEntity<PostPageResponseDTO> getMainNoticePost(){
        return ResponseEntity.ok(postService.findMainNoticePostResponse());
    }

    @PostMapping("/{id}/thumbs-up")
    public ResponseEntity<PostThumbsResponseDTO> postThumbsUp(@PathVariable long id, @AuthenticationPrincipal CustomUser customUser){
        return ResponseEntity.ok(postService.findPostAndThumbsUpResponse(id, customUser.getUser()));
    }

    @PostMapping("/{id}/thumbs-down")
    public ResponseEntity<PostThumbsResponseDTO> postThumbsDown(@PathVariable long id, @AuthenticationPrincipal CustomUser customUser){
        return ResponseEntity.ok(postService.findPostAndThumbsDownResponse(id, customUser.getUser()));
    }

    @PostMapping
    public void createPost(@RequestBody @Valid PostDTO postDTO, @AuthenticationPrincipal CustomUser customUser){
        postService.createPost(postDTO, customUser.getUser());
    }

    // NOTE: Comment 함수

    @GetMapping("/comment")
    public void getComment(@RequestParam long postId, @RequestParam(required = false, defaultValue = "0") int page){
        postService.getCommentPageResponse(postId, page);
    }

    @PostMapping("/comment")
    public void createComment(@RequestBody CommentDTO commentDTO, @AuthenticationPrincipal CustomUser customUser){
        postService.createComment(commentDTO, customUser.getUser());
    }
}
