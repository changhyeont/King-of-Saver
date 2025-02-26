package changhyeon.mybudgetcommunity.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {
        private final String code;
        private final String message;
        private final String path;
        private final LocalDateTime timestamp;

        public static ResponseEntity<ErrorResponse> toResponseEntity(
                        ErrorCode errorCode,
                        String path) {
                ErrorResponse response = ErrorResponse.builder()
                                .message(errorCode.getMessage())
                                .code(errorCode.getCode())
                                .path(path)
                                .timestamp(LocalDateTime.now())
                                .build();
                return new ResponseEntity<>(response, errorCode.getHttpStatus()); // getStatus() -> getHttpStatus()
        }
}