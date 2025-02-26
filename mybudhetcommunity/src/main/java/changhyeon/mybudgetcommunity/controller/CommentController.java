package changhyeon.mybudgetcommunity.controller;

import changhyeon.mybudgetcommunity.dto.ApiResponse;
import changhyeon.mybudgetcommunity.dto.CommentDto;
import changhyeon.mybudgetcommunity.security.UserDetailsImpl;
import changhyeon.mybudgetcommunity.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Comment", description = "댓글 API")
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "댓글 작성", description = "새로운 댓글을 작성합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<CommentDto>> createComment(
            @AuthenticationPrincipal UserDetailsImpl user,  // 이름 수정
            @RequestBody @Valid CommentDto.CreateRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(
                commentService.createComment(user.getId(), request)));
    }

    @Operation(summary = "댓글 수정", description = "댓글을 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentDto>> updateComment(
            @AuthenticationPrincipal UserDetailsImpl user,
            @PathVariable Long id,
            @RequestBody @Valid CommentDto.UpdateRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(
                commentService.updateComment(user.getId(), id, request)));
    }

    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @AuthenticationPrincipal UserDetailsImpl user,
            @PathVariable Long id) {
        commentService.deleteComment(user.getId(), id);
        return ResponseEntity.ok(new ApiResponse<>(null, "댓글이 삭제되었습니다."));
    }
}