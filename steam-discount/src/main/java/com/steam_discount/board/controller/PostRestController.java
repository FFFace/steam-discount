package com.steam_discount.board.controller;


import com.steam_discount.board.entity.dto.CommentDTO;
import com.steam_discount.board.entity.dto.PostDTO;
import com.steam_discount.board.entity.responseDTO.CommentPageResponseDTO;
import com.steam_discount.board.entity.responseDTO.CommentResponseDTO;
import com.steam_discount.board.entity.responseDTO.PostPageListResponseDTO;
import com.steam_discount.board.entity.responseDTO.PostPageResponseDTO;
import com.steam_discount.board.entity.responseDTO.PostResponseDTO;
import com.steam_discount.board.entity.responseDTO.PostThumbsResponseDTO;
import com.steam_discount.board.service.PostService;
import com.steam_discount.common.security.jwt.user.CustomUser;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostRestController {

    private final PostService postService;

    /**
     * 해당하는 게시판의 게시글을 페이지에 따라 10개 찾습니다.
     * @param boardId 게시판 id
     * @param page 페이지
     * @return PostPageListResponseDTO
     */
    @GetMapping
    public ResponseEntity<PostPageListResponseDTO> getPosts(@RequestParam int boardId, @RequestParam(required = false, defaultValue = "0") int page){
        return ResponseEntity.ok(postService.findPostListByBoardIdResponse(boardId, page));
    }

    /**
     * 특정 게시글을 찾습니다.
     * @param id 찾을 게시글 id
     * @return PostResponseDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPost(@PathVariable long id){
        return ResponseEntity.ok(postService.findPostByIdResponse(id));
    }

    /**
     * 메인 페이지에서 사용할 가장 최근 공지사항 게시글을 찾습니다.
     * @return PostPageResponseDTO
     */
    @GetMapping("/main-notice")
    public ResponseEntity<PostPageResponseDTO> getMainNoticePost(){
        return ResponseEntity.ok(postService.findNewNoticeForMain());
    }

    @GetMapping("/main-new-post-list")
    public ResponseEntity<List<PostPageResponseDTO>> getMainNewPostList(){
        return ResponseEntity.ok(postService.findNewPostForMain());
    }

    /**
     * 해당 게시글에 추천 수를 올립니다.
     * @param id 해당 게시글 id
     * @param customUser 사용자
     * @return PostThumbsResponseDTO
     */
    @PostMapping("/{id}/thumbs-up")
    public ResponseEntity<PostThumbsResponseDTO> postThumbsUp(@PathVariable long id, @AuthenticationPrincipal CustomUser customUser){
        return ResponseEntity.ok(postService.findPostAndThumbsUpResponse(id, customUser.getUser()));
    }

    /**
     * 사용자가 작성한 게시글을 검색합니다.
     * @param page 페이지
     * @param customUser 사용자
     * @return PostPageListResponseDTO
     */
    @GetMapping("/writed-post-list")
    public ResponseEntity<PostPageListResponseDTO> getWritedPostList(@RequestParam(required = false, defaultValue = "0") int page, @AuthenticationPrincipal CustomUser customUser){
        return ResponseEntity.ok(postService.findWritedPostResponse(customUser.getUser(), page));
    }

    @PostMapping("/{id}/thumbs-down")
    public ResponseEntity<PostThumbsResponseDTO> postThumbsDown(@PathVariable long id, @AuthenticationPrincipal CustomUser customUser){
        return ResponseEntity.ok(postService.findPostAndThumbsDownResponse(id, customUser.getUser()));
    }

    @PostMapping("/create")
    public long createPost(@RequestBody @Valid PostDTO postDTO, @AuthenticationPrincipal CustomUser customUser){
        return postService.createPost(postDTO, customUser.getUser());
    }

    @PutMapping("/{id}")
    public void updatePost(@PathVariable long id, @RequestBody @Valid PostDTO postDTO, @AuthenticationPrincipal CustomUser customUser){
        postService.updatePost(id, postDTO, customUser.getUser());
    }

    @PutMapping("/disable/{id}")
    public void disablePost(@PathVariable long id, @AuthenticationPrincipal CustomUser customUser){
        postService.disablePost(id, customUser.getUser());
    }

    @GetMapping("/disable/{id}")
    public ResponseEntity<Boolean> postDisableCheck(@PathVariable long id){
        return ResponseEntity.ok(postService.postDisableCheck(id));
    }

    @GetMapping("/firebase/upload-url")
    public ResponseEntity<String> getUploadURL(@RequestParam String contentType){
        return ResponseEntity.ok(postService.getFirebaseUploadUrl(contentType));
    }

    @GetMapping("/firebase/blob-make-public")
    public void getImageURL(@RequestParam String blobName){
        postService.firebaseImageMakePublic(blobName);
    }

    @DeleteMapping("/firebase/delete")
    public void deleteImage(@RequestParam String blobName){
        postService.deleteFirebaseImage(blobName);
    }


    // NOTE: Comment 함수

    @GetMapping("/{postId}/comments")
    public CommentPageResponseDTO getComment(@PathVariable long postId, @RequestParam(required = false, defaultValue = "0") int page){
        return postService.getCommentPageResponse(postId, page);
    }

    @GetMapping("/comments/{parentId}/reply")
    public CommentPageResponseDTO getCommentReply(@PathVariable long parentId, @RequestParam(required = false, defaultValue = "0") int page){
        return postService.getReplyCommentPageResponse(parentId, page);
    }

    @PostMapping("/comments")
    public CommentResponseDTO createComment(@RequestBody @Valid CommentDTO commentDTO, @AuthenticationPrincipal CustomUser customUser){
        return postService.createComment(commentDTO, customUser.getUser());
    }

    @PutMapping("/comments/disable/{id}")
    public void disableComment(@PathVariable long id, @AuthenticationPrincipal CustomUser customUser){
        postService.disableComment(id, customUser.getUser());
    }
}
