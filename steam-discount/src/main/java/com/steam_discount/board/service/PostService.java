package com.steam_discount.board.service;


import com.steam_discount.board.entity.Board;
import com.steam_discount.board.entity.Post;
import com.steam_discount.board.entity.PostThumbs;
import com.steam_discount.board.entity.dto.PostDTO;
import com.steam_discount.board.entity.responseDTO.PostPageListResponseDTO;
import com.steam_discount.board.entity.responseDTO.PostPageResponseDTO;
import com.steam_discount.board.entity.responseDTO.PostResponseDTO;
import com.steam_discount.board.entity.responseDTO.PostThumbsResponseDTO;
import com.steam_discount.board.repository.BoardRepository;
import com.steam_discount.board.repository.PostRepository;
import com.steam_discount.board.repository.PostThumbsRepository;
import com.steam_discount.common.exception.CustomException;
import com.steam_discount.common.exception.errorCode.ErrorCode;
import com.steam_discount.user.entity.User;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final BoardRepository boardRepository;
    private final PostThumbsRepository postThumbsRepository;

    private final int NOTICE_BOARD_NUMBER = 1;

    public PostPageListResponseDTO findPostListByBoardIdResponse(int boardId, int page ){
        Board board = boardRepository.findById(boardId).orElseThrow(() ->
            new CustomException(ErrorCode.NOT_FOUND_BOARD));

        int pageSize = 10;

        PageRequest pageRequest = PageRequest.of(page, pageSize);
        Page<Post> postPage = postRepository.findByBoardId(boardId, pageRequest);
        List<PostPageResponseDTO> postPageResponseDTOList = new ArrayList<>();

        postPage.get().forEach(post -> postPageResponseDTOList.add(post.toPageResponseDTO()));
        PostPageListResponseDTO postPageListResponseDTO = new PostPageListResponseDTO(postPageResponseDTOList, postPage.getTotalPages());

        return postPageListResponseDTO;
    }


    public PostResponseDTO findPostByIdResponse(long id){
        Post post = findPostById(id);

        return post.toPostResponseDTO();
    }


    public PostPageResponseDTO findMainNoticePostResponse(){
        return postRepository.findLastByBoardId(NOTICE_BOARD_NUMBER).orElseThrow(() ->
            new CustomException(ErrorCode.NOT_FOUND_POST)).toPageResponseDTO();
    }

    /** 전달 받은 post 값으로 PostThumbs 를 검색합니다.
     * 검색할 데이터가 없으면 새로운 게시글 추천 데이터를 생성합니다.
     * 검색된 데이터가 있으면 기존 데이터를 게시글 추천으로 수정 후 반영합니다.
     * @param id post id
     * @param user 로그인 중인 사용자
     * @return 게시글 추천, 비추천 수
     */
    public PostThumbsResponseDTO findPostAndThumbsUpResponse(long id, User user){
        PostThumbs postThumbs = postThumbsRepository.findByPostIdAndUserId(id, user.getId()).orElse(null);
        PostThumbsResponseDTO postThumbsResponseDTO = new PostThumbsResponseDTO();

        if(postThumbs != null){
            Post post = postThumbs.getPost();

            if(postThumbs.getThumb() == 'U'){
                postThumbsDelete(postThumbs);

                postThumbsResponseDTO.setThumbsUp(post.getThumbsUp()-1);
                postThumbsResponseDTO.setThumbsDown(post.getThumbsDown());
            } else if(postThumbs.getThumb() == 'D'){
                postThumbs.setThumb('U');
                postThumbsSave(postThumbs);

                postThumbsResponseDTO.setThumbsUp(post.getThumbsUp()+1);
                postThumbsResponseDTO.setThumbsDown(post.getThumbsDown()-1);
            }
        } else{
            PostThumbs newPostThumbs = new PostThumbs();
            Post post = findPostById(id);
            newPostThumbs.setPost(post);
            newPostThumbs.setThumb('U');
            newPostThumbs.setUserId(user.getId());

            postThumbsSave(newPostThumbs);

            postThumbsResponseDTO.setThumbsUp(post.getThumbsUp()+1);
            postThumbsResponseDTO.setThumbsDown(post.getThumbsDown());
        }

        return postThumbsResponseDTO;
    }

    private void postThumbsSave(PostThumbs postThumbs){
        postThumbsRepository.save(postThumbs);
    }

    private void postThumbsDelete(PostThumbs postThumbs){
        postThumbsRepository.delete(postThumbs);
    }

    public void createPost(PostDTO postDTO, User user){
        Board board = boardRepository.findById(postDTO.getBoardId()).orElseThrow(() ->
            new CustomException(ErrorCode.NOT_FOUND_BOARD));

        Post post = postDTO.toEntity();
        post.setWriter(user);
        post.setBoard(board);

        postRepository.save(post);
    }

    private Post findPostById(long id){
        return postRepository.findById(id).orElseThrow(()->
            new CustomException(ErrorCode.NOT_FOUND_POST));
    }
}
