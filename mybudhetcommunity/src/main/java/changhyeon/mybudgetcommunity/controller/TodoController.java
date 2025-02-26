package changhyeon.mybudgetcommunity.controller;

import changhyeon.mybudgetcommunity.dto.ApiResponse;
import changhyeon.mybudgetcommunity.dto.TodoDto;
import changhyeon.mybudgetcommunity.security.UserDetailsImpl;
import changhyeon.mybudgetcommunity.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Todo", description = "할 일 API")
@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;

    @Operation(summary = "할 일 목록 조회", description = "특정 날짜의 할 일 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<TodoDto>>> getTodosByDate(
            @AuthenticationPrincipal UserDetailsImpl user,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(new ApiResponse<>(
                todoService.getTodosByDate(user.getId(), date)));
    }

    @Operation(summary = "할 일 작성", description = "새로운 할 일을 작성합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<TodoDto>> createTodo(
            @AuthenticationPrincipal UserDetailsImpl user,
            @RequestBody @Valid TodoDto.CreateRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(
                todoService.createTodo(user.getId(), request)));
    }

    @Operation(summary = "할 일 수정", description = "할 일을 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TodoDto>> updateTodo(
            @AuthenticationPrincipal UserDetailsImpl user,
            @PathVariable Long id,
            @RequestBody @Valid TodoDto.UpdateRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(
                todoService.updateTodo(user.getId(), id, request)));
    }

    @Operation(summary = "할 일 삭제", description = "할 일을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTodo(
            @AuthenticationPrincipal UserDetailsImpl user,
            @PathVariable Long id) {
        todoService.deleteTodo(user.getId(), id);
        return ResponseEntity.ok(new ApiResponse<>(null, "할 일이 삭제되었습니다."));
    }
}