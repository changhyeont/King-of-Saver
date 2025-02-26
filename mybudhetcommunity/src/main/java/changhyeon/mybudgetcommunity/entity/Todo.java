package changhyeon.mybudgetcommunity.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

import changhyeon.mybudgetcommunity.exception.CustomException;
import changhyeon.mybudgetcommunity.exception.ErrorCode;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "todos")
public class Todo extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDate date;

    private BigDecimal amount;

    @Builder
    public Todo(User user, String title, LocalDate date, BigDecimal amount) {
        this.user = user;
        this.title = title;
        this.date = date;
        this.amount = amount;
    }

    public void update(String title, LocalDate date, BigDecimal amount) {
        if (title == null || title.trim().isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }
        if (date == null) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }
        
        this.title = title;
        this.date = date;
        this.amount = amount;
    }
}
