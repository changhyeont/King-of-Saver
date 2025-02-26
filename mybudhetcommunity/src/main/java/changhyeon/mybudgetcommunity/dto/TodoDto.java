package changhyeon.mybudgetcommunity.dto;

import changhyeon.mybudgetcommunity.entity.Todo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
public class TodoDto {
    private Long id;
    private String title;
    private LocalDate date;
    private BigDecimal amount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Getter @Setter
    @NoArgsConstructor
    public static class CreateRequest {
        @NotBlank(message = "제목은 필수입니다.")
        private String title;

        @NotNull(message = "날짜는 필수입니다.")
        private LocalDate date;

        private BigDecimal amount;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class UpdateRequest {
        @NotNull(message = "제목은 필수입니다.")
        @NotBlank(message = "제목은 비워둘 수 없습니다.")
        private String title;

        @NotNull(message = "날짜는 필수입니다.")
        private LocalDate date;

        @NotNull(message = "금액은 필수입니다.")
        @PositiveOrZero(message = "금액은 0 이상이어야 합니다.")
        private BigDecimal amount;
    }

    public static TodoDto from(Todo todo) {
        TodoDto dto = new TodoDto();
        dto.setId(todo.getId());
        dto.setTitle(todo.getTitle());
        dto.setDate(todo.getDate());
        dto.setAmount(todo.getAmount());
        dto.setCreatedAt(todo.getCreatedAt());
        dto.setUpdatedAt(todo.getUpdatedAt());
        return dto;
    }
}
