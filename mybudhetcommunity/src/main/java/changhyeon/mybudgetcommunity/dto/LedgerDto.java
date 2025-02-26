package changhyeon.mybudgetcommunity.dto;

import changhyeon.mybudgetcommunity.entity.Ledger;
import changhyeon.mybudgetcommunity.enums.ExpenseCategory;
import changhyeon.mybudgetcommunity.enums.IncomeCategory;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Map;
import java.time.YearMonth;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class LedgerDto {
    private Long id;
    private LocalDate date;
    private BigDecimal income;
    private BigDecimal expense;
    private ExpenseCategory expenseCategory;
    private IncomeCategory incomeCategory;
    private String description;

    @Builder
    public LedgerDto(Long id, LocalDate date, BigDecimal income, BigDecimal expense,
            ExpenseCategory expenseCategory, IncomeCategory incomeCategory,
            String description) {
        this.id = id;
        this.date = date;
        this.income = income;
        this.expense = expense;
        this.expenseCategory = expenseCategory;
        this.incomeCategory = incomeCategory;
        this.description = description;
    }

    public static LedgerDto from(Ledger ledger) {
        return LedgerDto.builder()
                .id(ledger.getId())
                .date(ledger.getDate())
                .income(ledger.getIncome())
                .expense(ledger.getExpense())
                .expenseCategory(ledger.getExpenseCategory())
                .incomeCategory(ledger.getIncomeCategory())
                .description(ledger.getDescription())
                .build();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class CreateRequest {
        @NotNull(message = "날짜는 필수입니다")
        private LocalDate date;

        @NotNull(message = "금액은 필수입니다")
        @PositiveOrZero(message = "금액은 0 이상이어야 합니다")
        private BigDecimal amount;

        private boolean isIncome;
        private IncomeCategory incomeCategory;
        private ExpenseCategory expenseCategory;
        private String description;

        @Builder
        public CreateRequest(LocalDate date, BigDecimal amount, 
                            boolean isIncome, 
                            IncomeCategory incomeCategory,
                            ExpenseCategory expenseCategory,
                            String description) {
            this.date = date;
            this.amount = amount;
            this.isIncome = isIncome;
            this.incomeCategory = incomeCategory;
            this.expenseCategory = expenseCategory;
            this.description = description;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class UpdateRequest {
        @NotNull(message = "날짜는 필수입니다.")
        private LocalDate date;

        @NotNull(message = "수입은 필수입니다.")
        @PositiveOrZero(message = "수입은 0 이상이어야 합니다.")
        private BigDecimal income;

        @NotNull(message = "지출은 필수입니다.")
        @PositiveOrZero(message = "지출은 0 이상이어야 합니다.")
        private BigDecimal expense;
    }

    @Getter
    public static class MonthlyStats {
        private final BigDecimal totalIncome;
        private final BigDecimal totalExpense;
        private final BigDecimal balance;
        private final YearMonth yearMonth;

        public MonthlyStats(BigDecimal totalIncome, BigDecimal totalExpense,
                YearMonth yearMonth) {
            this.totalIncome = totalIncome;
            this.totalExpense = totalExpense;
            this.balance = totalIncome.subtract(totalExpense);
            this.yearMonth = yearMonth;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class YearlyStats {
        private BigDecimal totalIncome;
        private BigDecimal totalExpense;
        private BigDecimal savings;
        private Map<Integer, BigDecimal> monthlySavings;

        // 필요한 모든 필드를 포함하는 생성자 추가
        public YearlyStats(
                BigDecimal totalIncome,
                BigDecimal totalExpense,
                BigDecimal savings,
                Map<Integer, BigDecimal> monthlySavings) {
            this.totalIncome = totalIncome;
            this.totalExpense = totalExpense;
            this.savings = savings;
            this.monthlySavings = monthlySavings;
        }
    }
}