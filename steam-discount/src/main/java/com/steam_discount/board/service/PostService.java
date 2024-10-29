package com.steam_discount.board.service;


import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.HttpMethod;
import com.google.cloud.storage.Storage;
import com.steam_discount.board.entity.Board;
import com.steam_discount.board.entity.Comment;
import com.steam_discount.board.entity.Post;
import com.steam_discount.board.entity.PostThumbs;
import com.steam_discount.board.entity.dto.CommentDTO;
import com.steam_discount.board.entity.dto.PostDTO;
import com.steam_discount.board.entity.responseDTO.CommentPageResponseDTO;
import com.steam_discount.board.entity.responseDTO.CommentResponseDTO;
import com.steam_discount.board.entity.responseDTO.PostPageListResponseDTO;
import com.steam_discount.board.entity.responseDTO.PostPageResponseDTO;
import com.steam_discount.board.entity.responseDTO.PostResponseDTO;
import com.steam_discount.board.entity.responseDTO.PostThumbsResponseDTO;
import com.steam_discount.board.repository.BoardRepository;
import com.steam_discount.board.repository.CommentRepository;
import com.steam_discount.board.repository.PostRepository;
import com.steam_discount.board.repository.PostThumbsRepository;
import com.steam_discount.common.exception.CustomException;
import com.steam_discount.common.exception.errorCode.ErrorCode;
import com.steam_discount.user.entity.User;
import com.steam_discount.user.entity.UserRole;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final PostThumbsRepository postThumbsRepository;

    private final int NOTICE_BOARD_NUMBER = 1;
    private final int PAGE_SIZE = 10;

    private final Storage storage;

    @Value("${firebase.storage-bucket}")
    private String firebaseStorageBucket;

    /**
     * 특정 게시판의 게시글을 검색합니다.
     * 게시글을 찾아 10개씩 페이지 형태와 총 페이지 갯수를 넘겨줍니다.
     * @param boardId 검색할 게시판
     * @param page 페이지
     * @return PostPageListResponseDTO
     * @exception CustomException ErrorCode.NOT_FOUND_BOARD
     */
    public PostPageListResponseDTO findPostListByBoardIdResponse(int boardId, int page){
        Board board = boardRepository.findById(boardId).orElseThrow(() ->
            new CustomException(ErrorCode.NOT_FOUND_BOARD));

        PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE);
        Page<Post> postPage = postRepository.findByBoardIdAndDisableIsNull(boardId, pageRequest);
        List<PostPageResponseDTO> postPageResponseDTOList = new ArrayList<>();

        postPage.get().forEach(post -> postPageResponseDTOList.add(post.toPageResponseDTO()));

        return new PostPageListResponseDTO(postPageResponseDTOList, postPage.getTotalPages());
    }

    /**
     * 특정 게시글을 검색합니다.
     * @param id 검색할 게시글
     * @return PostResponseDTO
     */
    public PostResponseDTO findPostByIdResponse(long id){
        Post post = findPostById(id);

        if(post.getDisable() != null){
            throw new CustomException(ErrorCode.DISABLE_POST);
        }
        return post.toPostResponseDTO();
    }

    /**
     * 메인 페이지에서 사용할 가장 최근 공지사항을 검색합니다.
     * @return PostPageResponseDTO
     */
    public PostPageResponseDTO findNewNoticeForMain(){
        return postRepository.findFirstByBoardIdOrderByCreatedAtDesc(NOTICE_BOARD_NUMBER).orElseThrow(() ->
            new CustomException(ErrorCode.NOT_FOUND_POST)).toPageResponseDTO();
    }

    public List<PostPageResponseDTO> findNewPostForMain(){
        List<Post> postList = postRepository.findOrderByCreatedAtDescLimit10();
        List<PostPageResponseDTO> postPageResponseDTOList = new ArrayList<>();

        postList.forEach(post -> {
            postPageResponseDTOList.add(post.toPageResponseDTO());
        });

        return postPageResponseDTOList;
    }

    /**
     * 사용자가 작성했던 게시글을 검색합니다.
     * @param user 검색할 사용자
     * @param page 페이지
     * @return PostPageListResponseDTO
     */
    public PostPageListResponseDTO findWritedPostResponse(User user, int page){
        PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE);
        Page<Post> postPage = postRepository.findByWriterOrderByBoardIdAscIdAsc(user, pageRequest);
        List<PostPageResponseDTO> postPageResponseDTOList = new ArrayList<>();

        postPage.get().forEach(post -> postPageResponseDTOList.add(post.toPageResponseDTO()));
        return new PostPageListResponseDTO(postPageResponseDTOList, postPage.getTotalPages());
    }



    /** 게시글 추천 기능 함수 입니다.<br/>
     * 전달 받은 post 값으로 PostThumbs 를 검색합니다.<br/>
     * 검색된 데이터가 이미 게시글 추천 상태이면 해당 데이터를 삭제합니다.<br/>
     * 검색된 데이터가 게시글 비추천 상태이면 추천 상태로 수정합니다.<br/>
     * 검색된 데이터가 없으면 게시글 추천 데이터를 생성합니다.
     * @param id post id
     * @param user 검증된 사용자
     * @return 게시글 추천, 비추천 수
     */
    @Transactional
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

        postThumbsResponseDTO.setThumb('U');

        return postThumbsResponseDTO;
    }

    /** 게시글 비추천 기능 함수 입니다.<br/>
     * 전달 받은 post 값으로 PostThumbs 를 검색합니다.<br/>
     * 검색된 데이터가 이미 게시글 비추천 상태이면 해당 데이터를 삭제합니다.<br/>
     * 검색된 데이터가 게시글 추천 상태이면 추천 상태로 수정합니다.<br/>
     * 검색된 데이터가 없으면 게시글 비추천 데이터를 생성합니다.
     * @param id post id
     * @param user 검증된 사용자
     * @return 게시글 추천, 비추천 수
     */
    @Transactional
    public PostThumbsResponseDTO findPostAndThumbsDownResponse(long id, User user){
        PostThumbs postThumbs = postThumbsRepository.findByPostIdAndUserId(id, user.getId()).orElse(null);
        PostThumbsResponseDTO postThumbsResponseDTO = new PostThumbsResponseDTO();

        if(postThumbs != null){
            Post post = postThumbs.getPost();

            if(postThumbs.getThumb() == 'D'){
                postThumbsDelete(postThumbs);

                postThumbsResponseDTO.setThumbsUp(post.getThumbsUp());
                postThumbsResponseDTO.setThumbsDown(post.getThumbsDown()-1);
            } else if(postThumbs.getThumb() == 'U'){
                postThumbs.setThumb('D');
                postThumbsSave(postThumbs);

                postThumbsResponseDTO.setThumbsUp(post.getThumbsUp()-1);
                postThumbsResponseDTO.setThumbsDown(post.getThumbsDown()+1);
            }
        } else{
            PostThumbs newPostThumbs = new PostThumbs();
            Post post = findPostById(id);
            newPostThumbs.setPost(post);
            newPostThumbs.setThumb('D');
            newPostThumbs.setUserId(user.getId());

            postThumbsSave(newPostThumbs);

            postThumbsResponseDTO.setThumbsUp(post.getThumbsUp());
            postThumbsResponseDTO.setThumbsDown(post.getThumbsDown()+1);
        }

        postThumbsResponseDTO.setThumb('D');

        return postThumbsResponseDTO;
    }

    private void postThumbsSave(PostThumbs postThumbs){
        postThumbsRepository.save(postThumbs);
    }

    private void postThumbsDelete(PostThumbs postThumbs){
        postThumbsRepository.delete(postThumbs);
    }

    /**
     * 새로운 게시글을 생성합니다.
     * @param postDTO 새로 생성할 게시글 정보
     * @param user 로그인 한 사용자
     */
    public long createPost(PostDTO postDTO, User user){
        Board board = boardRepository.findById(postDTO.getBoardId()).orElseThrow(() ->
            new CustomException(ErrorCode.NOT_FOUND_BOARD));

        Post post = postDTO.toEntity();
        post.setWriter(user);
        post.setBoard(board);
        return postRepository.save(post).getId();
    }

    /**
     * 기존 게시글을 수정합니다.
     * @param id 수정할 게시글 id
     * @param postDTO 수정할 게시글 정보
     * @param user 로그인 한 사용자
     */
    public void updatePost(long id, PostDTO postDTO, User user){
        Post oldPost = findPostById(id);

        if(user.getRole() != UserRole.ADMIN){
            if(oldPost.getWriter().getId() != user.getId())
                throw new CustomException(ErrorCode.NOT_MATCH_USER_FOR_UPDATE_POST);
        }


        oldPost.setName(postDTO.getName());
        oldPost.setContent(postDTO.getContent());

        postRepository.save(oldPost);
    }

    public void disablePost(long id, User user){
        Post post = findPostById(id);

        if(user.getRole() != UserRole.ADMIN){
            if(user.getId() != post.getWriter().getId()){
                throw new CustomException(ErrorCode.NOT_MATCH_USER_FOR_UPDATE_POST);
            }
        }

        post.isDisable();
        postRepository.save(post);
    }

    public boolean postDisableCheck(long id){
        Post post = findPostById(id);

        return post.getDisable() == null;
    }

    public String getFirebaseUploadUrl(String contentType){
        String blobName = "images/" + UUID.randomUUID();
        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(firebaseStorageBucket, blobName)).build();

        Map<String, String> extensionHeaders = new HashMap<>();
        extensionHeaders.put("Content-Type", contentType);

        return storage.signUrl(
            blobInfo,
            5,
            TimeUnit.MINUTES,
            Storage.SignUrlOption.withExtHeaders(extensionHeaders),
            Storage.SignUrlOption.httpMethod(HttpMethod.PUT),
            Storage.SignUrlOption.withV4Signature()).toString();
    }

    /**
     * id 값을 기준으로 post를 찾습니다.
     * @param id
     * @return Post
     * @exception CustomException not found post
     */
    private Post findPostById(long id){
        return postRepository.findById(id).orElseThrow(()->
            new CustomException(ErrorCode.NOT_FOUND_POST));
    }






    // NOTE: ---------- Comment 함수 ----------


    /**
     * 게시글의 댓글들을 페이지 형태로 리턴 합니다.
     * @param postId 게시글 id
     * @param page 페이지
     * @return CommentPageResponseDTO
     */
    public CommentPageResponseDTO getCommentPageResponse(long postId, int page){
        PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE);

        Page<Comment> commentPage = commentRepository.findByPostIdAndParentIdIsNullAndDisableIsNull(postId, pageRequest);
        List<CommentResponseDTO> commentResponseDTOList = new ArrayList<>();

        commentPage.get().forEach(comment -> commentResponseDTOList.add(comment.toResponseDTO()));
        CommentPageResponseDTO commentPageRespopnseDTO = new CommentPageResponseDTO();

        commentResponseDTOList.forEach(commentResponseDTO -> {
            CommentPageResponseDTO replyPageResponseDTO = getReplyCommentPageResponse(commentResponseDTO.getId(), 0);
            commentResponseDTO.setReplyCommentPageResponseDTO(replyPageResponseDTO);
        });

        commentPageRespopnseDTO.setCommentResponseDTOList(commentResponseDTOList);
        commentPageRespopnseDTO.setTotalPage(commentPage.getTotalPages());
        commentPageRespopnseDTO.setTotalElement(commentPage.getTotalElements());

        return commentPageRespopnseDTO;
    }

    /**
     * 댓글의 대댓글을 페이지 형태로 리턴합니다.
     * @param parentId 댓글 id
     * @param page 페이지
     * @return CommentPageResponseDTO
     */
    public CommentPageResponseDTO getReplyCommentPageResponse(long parentId, int page){
        PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE);

        Page<Comment> commentPage = commentRepository.findByParentId(parentId, pageRequest);
        List<CommentResponseDTO> commentResponseDTOList = new ArrayList<>();

        commentPage.get().forEach(comment -> commentResponseDTOList.add(comment.toResponseDTO()));
        CommentPageResponseDTO commentPageRespopnseDTO = new CommentPageResponseDTO();

        commentPageRespopnseDTO.setCommentResponseDTOList(commentResponseDTOList);
        commentPageRespopnseDTO.setTotalPage(commentPage.getTotalPages());

        return commentPageRespopnseDTO;
    }

    public CommentResponseDTO createComment(CommentDTO commentDTO, User user){
        Post post = findPostById(commentDTO.getPostId());

        Comment comment = commentDTO.toEntity();
        comment.setPost(post);
        comment.setWriter(user);

        commentRepository.save(comment);

        return comment.toResponseDTO();
    }

    public void disableComment(long id, User user){
        Comment comment = findCommentById(id);

        if(user.getRole() != UserRole.ADMIN){
            if(user.getId() != comment.getWriter().getId()){
                throw new CustomException(ErrorCode.NOT_MATCH_USER_FOR_UPDATE_COMMENT);
            }
        }

        comment.isDisable();
        commentRepository.save(comment);
    }

    private Comment findCommentById(long id){
        return commentRepository.findById(id).orElseThrow(() ->
            new CustomException(ErrorCode.NOT_FOUND_COMMENT));
    }
}
