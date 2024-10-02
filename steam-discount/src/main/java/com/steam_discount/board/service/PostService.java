package com.steam_discount.board.service;


import com.steam_discount.board.entity.Board;
import com.steam_discount.board.entity.Post;
import com.steam_discount.board.entity.dto.PostDTO;
import com.steam_discount.board.entity.dto.PostPageDTO;
import com.steam_discount.board.entity.responseDTO.PostPageListResponseDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final BoardRepository boardRepository;

    public PostPageListResponseDTO findPostList(long boardId, int page ){
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
