package changhyeon.mybudgetcommunity.service;

import changhyeon.mybudgetcommunity.dto.PostDto;
import changhyeon.mybudgetcommunity.entity.Post;
import changhyeon.mybudgetcommunity.exception.ErrorCode;
import changhyeon.mybudgetcommunity.exception.CustomException;
import changhyeon.mybudgetcommunity.repository.PostRepository;
import changhyeon.mybudgetcommunity.repository.UserRepository;
import changhyeon.mybudgetcommunity.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Page<PostDto> getAllPosts(Pageable pageable) {
        return postRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(PostDto::from);
    }

    @Transactional
    public PostDto createPost(Long userId, PostDto.CreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Post post = Post.builder()
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .includeLedger(request.isIncludeLedger())
                .build();

        return PostDto.from(postRepository.save(post));
    }

    @Transactional
    public PostDto updatePost(Long userId, Long postId, PostDto.UpdateRequest request) {
        Post post = postRepository.findByIdWithUser(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        if (!post.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.NOT_POST_OWNER);
        }

        post.update(request.getTitle(), request.getContent(), request.isIncludeLedger());
        return PostDto.from(post);
    }

    @Transactional
    public void deletePost(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
            
        // 게시글 작성자 확인
        if (!post.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.NOT_POST_OWNER);
        }
        
        // 게시글 삭제
        postRepository.delete(post);
    }
}