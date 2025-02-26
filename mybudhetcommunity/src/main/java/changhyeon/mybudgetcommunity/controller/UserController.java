package changhyeon.mybudgetcommunity.controller;

import changhyeon.mybudgetcommunity.dto.ApiResponse;
import changhyeon.mybudgetcommunity.dto.UserDto;
import changhyeon.mybudgetcommunity.dto.TokenDto;
import changhyeon.mybudgetcommunity.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import changhyeon.mybudgetcommunity.security.JwtProvider;
import changhyeon.mybudgetcommunity.security.UserDetailsImpl;
import changhyeon.mybudgetcommunity.exception.CustomException;
import changhyeon.mybudgetcommunity.exception.ErrorCode;

@Tag(name = "User", description = "사용자 API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtProvider jwtProvider;

    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDto>> register(
            @RequestBody @Valid UserDto.RegisterRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(userService.register(request)));
    }

    @Operation(summary = "로그인", description = "사용자 인증을 수행합니다.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenDto>> login(
            @RequestBody @Valid UserDto.LoginRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(userService.login(request)));
    }

    @Operation(summary = "로그아웃", description = "사용자 로그아웃을 수행합니다.")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestHeader("Authorization") String token) {
        userService.logout(token);
        return ResponseEntity.ok(new ApiResponse<>(null, "로그아웃 성공"));
    }

    @GetMapping("/validate-token")
    public ResponseEntity<ApiResponse<Void>> validateToken(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        return ResponseEntity.ok(new ApiResponse<>(null, "토큰이 유효합니다."));
    }
}