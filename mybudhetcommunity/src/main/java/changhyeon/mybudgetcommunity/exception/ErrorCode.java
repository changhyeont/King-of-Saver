package changhyeon.mybudgetcommunity.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;

@Getter
public enum ErrorCode {
    // 인증 관련 에러
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "AUTH-001", "인증되지 않은 사용자입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH-002", "유효하지 않은 토큰입니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH-003", "만료된 토큰입니다."),
    
    // 사용자 관련 에러
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-001", "사용자를 찾을 수 없습니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "USER-002", "이미 존재하는 이메일입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "USER-003", "비밀번호가 일치하지 않습니다."),
    
    // 가계부 관련 에러
    LEDGER_NOT_FOUND(HttpStatus.NOT_FOUND, "LEDGER-001", "가계부 항목을 찾을 수 없습니다."),
    NOT_LEDGER_OWNER(HttpStatus.FORBIDDEN, "LEDGER-002", "해당 가계부의 소유자가 아닙니다."),
    
    // 할 일 관련 에러
    TODO_NOT_FOUND(HttpStatus.NOT_FOUND, "TODO-001", "할 일을 찾을 수 없습니다."),
    NOT_TODO_OWNER(HttpStatus.FORBIDDEN, "TODO-002", "할 일의 작성자가 아닙니다."),
    
    // 댓글 관련 에러
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT-001", "댓글을 찾을 수 없습니다."),
    NOT_COMMENT_OWNER(HttpStatus.FORBIDDEN, "COMMENT-002", "댓글의 작성자가 아닙니다."),
    
    // 게시글 관련 에러
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST-001", "게시글을 찾을 수 없습니다."),
    NOT_POST_OWNER(HttpStatus.FORBIDDEN, "POST-002", "게시글의 작성자가 아닙니다."),
    
    // 입력값 검증 관련 에러
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "COMMON-001", "잘못된 입력값입니다"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "COMMON-002", "지원하지 않는 HTTP 메서드입니다"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON-003", "서버 오류가 발생했습니다");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}