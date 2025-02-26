package changhyeon.mybudgetcommunity.service;

import changhyeon.mybudgetcommunity.dto.LedgerDto;
import changhyeon.mybudgetcommunity.entity.Ledger;
import changhyeon.mybudgetcommunity.entity.User;
import changhyeon.mybudgetcommunity.exception.ErrorCode;
import changhyeon.mybudgetcommunity.exception.CustomException;
import changhyeon.mybudgetcommunity.repository.LedgerRepository;
import changhyeon.mybudgetcommunity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LedgerService {
        private final LedgerRepository ledgerRepository;
        private final UserRepository userRepository;

        public LedgerDto.YearlyStats getYearlyStats(Long userId, int year) {
                LocalDate startDate = LocalDate.of(year, 1, 1);
                LocalDate endDate = LocalDate.of(year, 12, 31);

                List<Ledger> yearlyLedgers = ledgerRepository
                                .findByUserIdAndDateBetweenOrderByDateAsc(userId, startDate, endDate);

                BigDecimal totalIncome = yearlyLedgers.stream()
                                .map(Ledger::getIncome)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal totalExpense = yearlyLedgers.stream()
                                .map(Ledger::getExpense)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                Map<Integer, BigDecimal> monthlySavings = yearlyLedgers.stream()
                                .collect(Collectors.groupingBy(
                                                ledger -> ledger.getDate().getMonthValue(),
                                                Collectors.reducing(
                                                                BigDecimal.ZERO,
                                                                ledger -> ledger.getIncome()
                                                                                .subtract(ledger.getExpense()),
                                                                BigDecimal::add)));

                return new LedgerDto.YearlyStats(
                                totalIncome,
                                totalExpense,
                                totalIncome.subtract(totalExpense),
                                monthlySavings);
        }

        public LedgerDto.MonthlyStats getMonthlyStats(Long userId, int year, int month) {
                // 해당 사용자의 특정 월 통계 조회
                List<Ledger> monthlyLedgers = ledgerRepository
                                .findByUserIdAndYearAndMonth(userId, year, month);

                BigDecimal totalIncome = monthlyLedgers.stream()
                                .map(Ledger::getIncome)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal totalExpense = monthlyLedgers.stream()
                                .map(Ledger::getExpense)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                return new LedgerDto.MonthlyStats(totalIncome, totalExpense, null);
        }

        @Transactional
        public LedgerDto createLedger(Long userId, LedgerDto.CreateRequest request) {
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

                // 수입/지출 금액 설정 수정
                BigDecimal income = request.isIncome() ? request.getAmount() : BigDecimal.ZERO;
                BigDecimal expense = !request.isIncome() ? request.getAmount() : BigDecimal.ZERO;

                // 로깅 추가
                System.out.println("isIncome: " + request.isIncome());
                System.out.println("amount: " + request.getAmount());
                System.out.println("income: " + income);
                System.out.println("expense: " + expense);

                Ledger ledger = Ledger.builder()
                                .user(user)
                                .date(request.getDate())
                                .income(income)      // 수정된 income 설정
                                .expense(expense)    // 수정된 expense 설정
                                .incomeCategory(request.isIncome() ? request.getIncomeCategory() : null)
                                .expenseCategory(!request.isIncome() ? request.getExpenseCategory() : null)
                                .description(request.getDescription())
                                .build();

                return LedgerDto.from(ledgerRepository.save(ledger));
        }

        @Transactional
        public LedgerDto updateLedger(Long userId, Long ledgerId, LedgerDto.UpdateRequest request) {
                Ledger ledger = ledgerRepository.findById(ledgerId)
                                .orElseThrow(() -> new CustomException(ErrorCode.LEDGER_NOT_FOUND));

                if (!ledger.getUser().getId().equals(userId)) {
                        throw new CustomException(ErrorCode.NOT_LEDGER_OWNER);
                }

                ledger.update(request.getDate(), request.getIncome(), request.getExpense());
                return LedgerDto.from(ledger);
        }

        @Transactional
        public void deleteLedger(Long userId, Long ledgerId) {
                Ledger ledger = ledgerRepository.findById(ledgerId)
                                .orElseThrow(() -> new CustomException(ErrorCode.LEDGER_NOT_FOUND));

                if (!ledger.getUser().getId().equals(userId)) {
                        throw new CustomException(ErrorCode.NOT_LEDGER_OWNER);
                }

                ledgerRepository.delete(ledger);
        }

        @Transactional(readOnly = true)
        public List<LedgerDto> getDailyLedgers(Long userId, LocalDate date) {
                List<Ledger> ledgers = ledgerRepository.findByUserIdAndDateOrderByCreatedAtDesc(userId, date);
                return ledgers.stream()
                                .map(LedgerDto::from)
                                .collect(Collectors.toList());
        }

        @Transactional(readOnly = true)
        public List<LedgerDto> getIncomeList(Long userId, LocalDate startDate, LocalDate endDate) {
                return ledgerRepository.findIncomesByUserIdAndDateBetween(userId, startDate, endDate)
                                .stream()
                                .map(LedgerDto::from)
                                .collect(Collectors.toList());
        }

        @Transactional
        public void deleteDailyLedgers(Long userId, LocalDate date) {
                List<Ledger> ledgers = ledgerRepository.findByUserIdAndDate(userId, date);
                ledgerRepository.deleteAll(ledgers);
        }

        @Transactional
        public void updateDailyLedger(Long userId, LocalDate date, LedgerDto.UpdateRequest request) {
                List<Ledger> ledgers = ledgerRepository.findByUserIdAndDate(userId, date);
                for (Ledger ledger : ledgers) {
                        ledger.update(date, request.getIncome(), request.getExpense());
                        ledgerRepository.save(ledger);
                }
        }
}