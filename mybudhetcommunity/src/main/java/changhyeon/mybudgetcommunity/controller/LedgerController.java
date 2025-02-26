package changhyeon.mybudgetcommunity.controller;

import changhyeon.mybudgetcommunity.dto.LedgerDto;
import changhyeon.mybudgetcommunity.service.LedgerService;
import changhyeon.mybudgetcommunity.dto.ApiResponse;
import changhyeon.mybudgetcommunity.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import changhyeon.mybudgetcommunity.exception.CustomException;
import changhyeon.mybudgetcommunity.exception.ErrorCode;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.List;

@Tag(name = "Ledger", description = "가계부 API")
@RestController
@RequestMapping("/api/ledgers")
@RequiredArgsConstructor
public class LedgerController {
        private final LedgerService ledgerService;

        @Operation(summary = "연간 통계 조회", description = "연도별 가계부 통계를 조회합니다.")
        @GetMapping("/stats/{year}")
        public ResponseEntity<ApiResponse<LedgerDto.YearlyStats>> getYearlyStats(
                        @AuthenticationPrincipal UserDetailsImpl user,
                        @PathVariable int year) {
                return ResponseEntity.ok(new ApiResponse<>(
                                ledgerService.getYearlyStats(user.getId(), year)));
        }

        @Operation(summary = "월간 통계 조회", description = "월별 가계부 통계를 조회합니다.")
        @GetMapping("/monthly/{year}/{month}")
        public ResponseEntity<ApiResponse<LedgerDto.MonthlyStats>> getMonthlyStats(
                        @AuthenticationPrincipal UserDetailsImpl user,
                        @PathVariable int year,
                        @PathVariable int month) {

                if (user == null) {
                        throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
                }

                return ResponseEntity.ok(new ApiResponse<>(
                                ledgerService.getMonthlyStats(user.getId(), year, month)));
        }

        @Operation(summary = "일별 내역 조회", description = "특정 날짜의 가계부 내역을 조회합니다.")
        @GetMapping("/daily/{date}")
        public ResponseEntity<ApiResponse<List<LedgerDto>>> getDailyLedgers(
                        @AuthenticationPrincipal UserDetailsImpl user,
                        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
                return ResponseEntity.ok(new ApiResponse<>(
                                ledgerService.getDailyLedgers(user.getId(), date)));
        }

        @Operation(summary = "가계부 작성", description = "새로운 가계부 항목을 작성합니다.")
        @PostMapping
        public ResponseEntity<ApiResponse<LedgerDto>> createLedger(
                        @AuthenticationPrincipal UserDetailsImpl user, // UserDetails -> UserDetailsImpl
                        @RequestBody @Valid LedgerDto.CreateRequest request) {
                return ResponseEntity.ok(new ApiResponse<>(
                                ledgerService.createLedger(user.getId(), request)));
        }

        @Operation(summary = "가계부 수정", description = "가계부 항목을 수정합니다.")
        @PutMapping("/{id}")
        public ResponseEntity<ApiResponse<LedgerDto>> updateLedger(
                        @AuthenticationPrincipal UserDetailsImpl user,
                        @PathVariable Long id,
                        @RequestBody @Valid LedgerDto.UpdateRequest request) {
                return ResponseEntity.ok(new ApiResponse<>(
                                ledgerService.updateLedger(user.getId(), id, request)));
        }

        @Operation(summary = "가계부 삭제", description = "가계부 항목을 삭제합니다.")
        @DeleteMapping("/{id}")
        public ResponseEntity<ApiResponse<Void>> deleteLedger(
                        @AuthenticationPrincipal UserDetailsImpl user,
                        @PathVariable Long id) {
                ledgerService.deleteLedger(user.getId(), id);
                return ResponseEntity.ok(new ApiResponse<>(null, "가계부 항목이 삭제되었습니다."));
        }

        @Operation(summary = "수입 내역 조회", description = "기간별 수입 내역을 조회합니다.")
        @GetMapping("/income")
        public ResponseEntity<ApiResponse<List<LedgerDto>>> getIncomeList(
                        @AuthenticationPrincipal UserDetailsImpl user,
                        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

                return ResponseEntity.ok(new ApiResponse<>(
                                ledgerService.getIncomeList(user.getId(), startDate, endDate)));
        }

        @Operation(summary = "당일 수입 및 지출 내역 수정", description = "특정 날짜의 모든 수입 및 지출 내역을 수정합니다.")
        @PutMapping("/daily/{date}")
        public ResponseEntity<ApiResponse<Void>> updateDailyLedger(
                        @AuthenticationPrincipal UserDetailsImpl user,
                        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                        @RequestBody @Valid LedgerDto.UpdateRequest request) {
                ledgerService.updateDailyLedger(user.getId(), date, request);
                return ResponseEntity.ok(new ApiResponse<>(null, "당일 수입 및 지출 내역이 수정되었습니다."));
        }

        @Operation(summary = "당일 수입 및 지출 내역 삭제", description = "특정 날짜의 모든 수입 및 지출 내역을 삭제합니다.")
        @DeleteMapping("/daily/{date}")
        public ResponseEntity<ApiResponse<Void>> deleteDailyLedgers(
                        @AuthenticationPrincipal UserDetailsImpl user,
                        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
                ledgerService.deleteDailyLedgers(user.getId(), date);
                return ResponseEntity.ok(new ApiResponse<>(null, "당일 수입 및 지출 내역이 삭제되었습니다."));
        }
}