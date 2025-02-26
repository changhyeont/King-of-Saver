package changhyeon.mybudgetcommunity.repository;

import changhyeon.mybudgetcommunity.entity.Ledger;
import changhyeon.mybudgetcommunity.dto.LedgerDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LedgerRepository extends JpaRepository<Ledger, Long> {
        List<Ledger> findByUserIdAndDateBetweenOrderByDateAsc(
                        Long userId, LocalDate startDate, LocalDate endDate);

        @Query("SELECT SUM(l.income) as totalIncome, SUM(l.expense) as totalExpense " +
                        "FROM Ledger l WHERE l.user.id = :userId AND YEAR(l.date) = :year " +
                        "AND MONTH(l.date) = :month")
        Optional<MonthlyStats> findMonthlyStatsByUserIdAndYearAndMonth(
                        @Param("userId") Long userId,
                        @Param("year") int year,
                        @Param("month") int month);

        // 방법 1: JPQL 사용
        @Query("SELECT l FROM Ledger l WHERE l.user.id = :userId " +
                        "AND YEAR(l.date) = :year " +
                        "AND MONTH(l.date) = :month")
        List<Ledger> findByUserIdAndYearAndMonth(
                        @Param("userId") Long userId,
                        @Param("year") int year,
                        @Param("month") int month);

        List<Ledger> findByUserIdAndDate(Long userId, LocalDate date);

        List<Ledger> findByUserIdAndDateOrderByCreatedAtDesc(Long userId, LocalDate date);

        @Query("SELECT SUM(l.income) as totalIncome, SUM(l.expense) as totalExpense " +
                        "FROM Ledger l WHERE l.user.id = :userId AND l.date = :date")
        Optional<MonthlyStats> findDailyStatsByUserIdAndDate(
                        @Param("userId") Long userId,
                        @Param("date") LocalDate date);

        @Query("SELECT l FROM Ledger l WHERE l.user.id = :userId AND l.date = :date " +
                        "ORDER BY l.createdAt DESC")
        List<Ledger> findByUserIdAndDateWithDetails(
                        @Param("userId") Long userId,
                        @Param("date") LocalDate date);

        @Query("SELECT new changhyeon.mybudgetcommunity.dto.LedgerDto(" +
                        "l.id, l.date, l.income, l.expense, " +
                        "l.expenseCategory, l.incomeCategory, l.description) " +
                        "FROM Ledger l WHERE l.user.id = :userId AND l.date = :date " +
                        "ORDER BY l.createdAt DESC")
        List<LedgerDto> findDailyLedgersByUserIdAndDate(
                        @Param("userId") Long userId,
                        @Param("date") LocalDate date);

        @Query("SELECT l FROM Ledger l WHERE l.user.id = :userId " +
                        "AND l.date BETWEEN :startDate AND :endDate " +
                        "AND l.income > 0 " +
                        "ORDER BY l.date DESC")
        List<Ledger> findIncomesByUserIdAndDateBetween(
                        @Param("userId") Long userId,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);
}

interface MonthlyStats {
        BigDecimal getTotalIncome();

        BigDecimal getTotalExpense();
}