package changhyeon.mybudgetcommunity.dto;

import changhyeon.mybudgetcommunity.entity.Post;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
@NoArgsConstructor
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private boolean includeLedger;
    private UserDto user;
    private List<CommentDto> comments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Getter @Setter
    @NoArgsConstructor
    public static class CreateRequest {
        @NotBlank(message = "제목은 필수입니다.")
        private String title;

        @NotBlank(message = "내용은 필수입니다.")
        private String content;

        private boolean includeLedger;
    }

    @Getter @Setter
    @NoArgsConstructor
    public static class UpdateRequest {
        @NotBlank(message = "제목은 필수입니다.")
        private String title;

        @NotBlank(message = "내용은 필수입니다.")
        private String content;

        private boolean includeLedger;
    }

    public static PostDto from(Post post) {
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setIncludeLedger(post.isIncludeLedger());
        dto.setUser(UserDto.from(post.getUser()));
        dto.setComments(post.getComments().stream()
                .map(CommentDto::from)
                .collect(Collectors.toList()));
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        return dto;
    }
}