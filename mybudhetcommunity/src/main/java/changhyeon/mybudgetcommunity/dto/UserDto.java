package changhyeon.mybudgetcommunity.dto;

import changhyeon.mybudgetcommunity.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Getter @Setter
    @NoArgsConstructor
    public static class RegisterRequest {
        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        private String email;

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Size(min = 6, message = "비밀번호는 6자 이상이어야 합니다.")
        private String password;
    }

    @Getter @Setter
    @NoArgsConstructor
    public static class LoginRequest {
        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        private String email;

        @NotBlank(message = "비밀번호는 필수입니다.")
        private String password;
    }

    public static UserDto from(User user) {
        if (user == null) {
            return null;
        }
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setCreatedAt(user.getCreatedAt());  // 타입 변환 불필요
        dto.setUpdatedAt(user.getUpdatedAt());  // 타입 변환 불필요
        return dto;
    }

    // 추가: 업데이트 요청을 위한 DTO
    @Getter @Setter
    @NoArgsConstructor
    public static class UpdateRequest {
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        private String email;
        
        @Size(min = 6, message = "비밀번호는 6자 이상이어야 합니다.")
        private String password;
    }
}