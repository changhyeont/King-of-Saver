package changhyeon.mybudgetcommunity.controller;

import changhyeon.mybudgetcommunity.dto.ApiResponse;
import changhyeon.mybudgetcommunity.dto.PostDto;
import changhyeon.mybudgetcommunity.security.UserDetailsImpl;
import changhyeon.mybudgetcommunity.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Post", description = "게시글 API")
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @Operation(summary = "게시글 목록 조회", description = "전체 게시글 목록을 페이징하여 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<PostDto>>> getPosts(Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse<>(postService.getAllPosts(pageable)));
    }

    @Operation(summary = "게시글 작성", description = "새로운 게시글을 작성합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<PostDto>> createPost(
            @AuthenticationPrincipal UserDetailsImpl user,
            @RequestBody @Valid PostDto.CreateRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(
                postService.createPost(user.getId(), request)));
    }

    @Operation(summary = "게시글 수정", description = "기존 게시글을 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PostDto>> updatePost(
            @AuthenticationPrincipal UserDetailsImpl user, // UserDetails -> UserDetailsImpl
            @PathVariable Long id,
            @RequestBody @Valid PostDto.UpdateRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(
                postService.updatePost(user.getId(), id, request)));
    }

    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @AuthenticationPrincipal UserDetailsImpl user,
            @PathVariable Long id) {
        postService.deletePost(user.getId(), id);
        return ResponseEntity.ok(new ApiResponse<>(null, "게시글이 삭제되었습니다."));
    }
}