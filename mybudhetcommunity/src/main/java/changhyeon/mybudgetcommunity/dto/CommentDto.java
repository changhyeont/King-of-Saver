package changhyeon.mybudgetcommunity.dto;

import changhyeon.mybudgetcommunity.entity.Comment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentDto {
    private Long id;
    private Long postId;
    private String content;
    private UserDto user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Getter @Setter
    @NoArgsConstructor
    public static class CreateRequest {
        @NotNull(message = "게시글 ID는 필수입니다.")
        private Long postId;
        
        @NotNull(message = "댓글 내용은 필수입니다.")
        @NotBlank(message = "댓글 내용은 비워둘 수 없습니다.")
        private String content;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class UpdateRequest {
        @NotNull(message = "댓글 내용은 필수입니다.")
        @NotBlank(message = "댓글 내용은 비워둘 수 없습니다.")
        private String content;
    }

    public static CommentDto from(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.id = comment.getId();
        dto.postId = comment.getPost().getId();
        dto.content = comment.getContent();
        dto.user = UserDto.from(comment.getUser());
        dto.createdAt = comment.getCreatedAt();
        dto.updatedAt = comment.getUpdatedAt();
        return dto;
    }
}
