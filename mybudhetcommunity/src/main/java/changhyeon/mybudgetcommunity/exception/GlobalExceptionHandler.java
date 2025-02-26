package changhyeon.mybudgetcommunity.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(CustomException.class)
        protected ResponseEntity<ErrorResponse> handleCustomException(
                        CustomException e,
                        HttpServletRequest request) {
                log.error("CustomException: {}", e.getMessage());
                return ErrorResponse.toResponseEntity(e.getErrorCode(), request.getRequestURI());
        }

        @ExceptionHandler(BindException.class)
        protected ResponseEntity<ErrorResponse> handleBindException(
                        BindException e,
                        HttpServletRequest request) {
                log.error("BindException: {}", e.getMessage());
                String errorMessage = e.getBindingResult()
                                .getAllErrors()
                                .stream()
                                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                .collect(Collectors.joining(", "));

                return ErrorResponse.toResponseEntity(
                                ErrorCode.INVALID_INPUT_VALUE,
                                request.getRequestURI());
        }

        @ExceptionHandler(Exception.class)
        protected ResponseEntity<ErrorResponse> handleException(
                        Exception e,
                        HttpServletRequest request) {
                log.error("Exception: ", e);
                return ErrorResponse.toResponseEntity(
                                ErrorCode.INTERNAL_SERVER_ERROR,
                                request.getRequestURI());
        }
}