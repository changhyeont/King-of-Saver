package changhyeon.mybudgetcommunity.service;

import changhyeon.mybudgetcommunity.dto.CommentDto;
import changhyeon.mybudgetcommunity.entity.Comment;
import changhyeon.mybudgetcommunity.entity.Post;
import changhyeon.mybudgetcommunity.entity.User;
import changhyeon.mybudgetcommunity.exception.CustomException;
import changhyeon.mybudgetcommunity.exception.ErrorCode;
import changhyeon.mybudgetcommunity.repository.CommentRepository;
import changhyeon.mybudgetcommunity.repository.PostRepository;
import changhyeon.mybudgetcommunity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public CommentDto createComment(Long userId, CommentDto.CreateRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
            
        Post post = postRepository.findById(request.getPostId())
            .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        Comment comment = Comment.builder()
            .content(request.getContent())
            .user(user)
            .post(post)
            .build();

        return CommentDto.from(commentRepository.save(comment));
    }

    @Transactional
    public CommentDto updateComment(Long userId, Long commentId, CommentDto.UpdateRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.NOT_COMMENT_OWNER);
        }

        comment.updateContent(request.getContent());
        return CommentDto.from(comment);
    }

    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.NOT_COMMENT_OWNER);
        }

        commentRepository.delete(comment);
    }
}
