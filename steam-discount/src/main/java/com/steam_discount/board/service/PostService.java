package com.steam_discount.board.service;


import com.steam_discount.board.entity.Board;
import com.steam_discount.board.entity.Post;
import com.steam_discount.board.entity.dto.PostDTO;
import com.steam_discount.board.entity.dto.PostPageDTO;
import com.steam_discount.board.entity.responseDTO.PostPageResponseDTO;
import com.steam_discount.board.entity.responseDTO.PostResponseDTO;
import com.steam_discount.board.repository.BoardRepository;
import com.steam_discount.board.repository.PostRepository;
import com.steam_discount.common.exception.CustomException;
import com.steam_discount.common.exception.errorCode.ErrorCode;
import com.steam_discount.user.entity.User;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final BoardRepository boardRepository;

    public List<PostPageResponseDTO> findPostList(PostPageDTO postPageDTO){
        Board board = boardRepository.findById(postPageDTO.getBoardId()).orElseThrow(() ->
            new CustomException(ErrorCode.NOT_FOUND_BOARD));

        int pageSize = 10;

        PageRequest pageRequest = PageRequest.of(postPageDTO.getPage(), pageSize);
        List<Post> postList = postRepository.findByBoardId(postPageDTO.getBoardId(), pageRequest);
        List<PostPageResponseDTO> postPageResponseDTOList = new ArrayList<>();
        for(Post post : postList){
            postPageResponseDTOList.add(post.toPageResponseDTO());
        }

        return postPageResponseDTOList;
    }

    public PostResponseDTO findPostById(Long id){
        Post post = postRepository.findById(id).orElseThrow(() ->
            new CustomException(ErrorCode.NOT_FOUND_POST));

        return post.toPostResponseDTO();
    }

    public void createPost(PostDTO postDTO, User user){
        Board board = boardRepository.findById(postDTO.getBoardId()).orElseThrow(() ->
            new CustomException(ErrorCode.NOT_FOUND_BOARD));

        Post post = postDTO.toEntity();
        post.setWriter(user);
        post.setBoard(board);

        postRepository.save(post);
    }
}
