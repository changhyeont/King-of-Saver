package changhyeon.mybudgetcommunity.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

import changhyeon.mybudgetcommunity.enums.ExpenseCategory;
import changhyeon.mybudgetcommunity.enums.IncomeCategory;
import changhyeon.mybudgetcommunity.exception.CustomException;
import changhyeon.mybudgetcommunity.exception.ErrorCode;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ledgers")
public class Ledger extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private BigDecimal income = BigDecimal.ZERO; // 기본값 설정

    @Column(nullable = false)
    private BigDecimal expense = BigDecimal.ZERO; // 기본값 설정

    @Enumerated(EnumType.STRING)
    private ExpenseCategory expenseCategory;

    @Enumerated(EnumType.STRING)
    private IncomeCategory incomeCategory;

    @Column(length = 500)
    private String description;

    @Builder
    public Ledger(User user, LocalDate date, BigDecimal expense, BigDecimal income,
            ExpenseCategory expenseCategory, IncomeCategory incomeCategory,
            String description) {
        this.user = user;
        this.date = date;
        this.expense = expense != null ? expense : BigDecimal.ZERO;
        this.income = income != null ? income : BigDecimal.ZERO;
        this.expenseCategory = expenseCategory;
        this.incomeCategory = incomeCategory;
        this.description = description;
    }

    public void update(LocalDate date, BigDecimal income, BigDecimal expense) {
        // 입력값 검증
        if (date == null) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }
        if (income == null || income.compareTo(BigDecimal.ZERO) < 0) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }
        if (expense == null || expense.compareTo(BigDecimal.ZERO) < 0) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }

        this.date = date;
        this.income = income != null ? income : this.income;
        this.expense = expense != null ? expense : this.expense;
    }
}