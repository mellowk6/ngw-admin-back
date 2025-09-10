package com.nhbank.ngw.api.shared.advice;

import com.nhbank.ngw.api.shared.dto.ApiError;
import com.nhbank.ngw.common.exception.DuplicateIdException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;

@RestControllerAdvice // = @ControllerAdvice + @ResponseBody
public class GlobalExceptionHandler {
    // 본 클래스에서는 401, 403에러와 같은 인증/인가 에러는 관리하지 않음

    // Bean Validation (DTO @Valid) 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest req) {
        var details = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new ApiError.FieldError(fe.getField(), fe.getDefaultMessage()))
                .toList();
        return build(req, HttpStatus.BAD_REQUEST, "요청 값이 올바르지 않습니다.", "VALIDATION_ERROR", details);
    }

    // @Validated + @RequestParam/@PathVariable 검증 실패
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest req) {
        var details = ex.getConstraintViolations().stream()
                .map(v -> new ApiError.FieldError(v.getPropertyPath().toString(), v.getMessage()))
                .toList();
        return build(req, HttpStatus.BAD_REQUEST, "요청 값이 올바르지 않습니다.", "CONSTRAINT_VIOLATION", details);
    }

    // 타입 변환 실패
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        return build(req, HttpStatus.BAD_REQUEST, "요청 파라미터 타입이 올바르지 않습니다.", "TYPE_MISMATCH", null);
    }

    // Spring 6+의 ErrorResponseException (ProblemDetail 기반)
    @ExceptionHandler(ErrorResponseException.class)
    public ResponseEntity<ApiError> handleErrorResponse(ErrorResponseException ex, HttpServletRequest req) {
        var statusCode = ex.getStatusCode();   // HttpStatusCode
        var pd = ex.getBody();                 // ProblemDetail (nullable)

        // ErrorResponseException에는 getReason()이 없음 → ProblemDetail의 title/detail 사용
        String title = (pd != null && pd.getTitle() != null) ? pd.getTitle() : ex.getMessage();

        var body = build(
                req,
                HttpStatus.valueOf(statusCode.value()),
                (title != null && !title.isBlank()) ? title : "요청 처리 중 오류가 발생했습니다.",
                "ERROR_RESPONSE",
                null // 필요하면 pd.getDetail()을 details로 매핑
        );
        return ResponseEntity.status(statusCode).body(body);
    }

    // ResponseStatusException
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> handleResponseStatus(ResponseStatusException ex, HttpServletRequest req) {
        var statusCode = ex.getStatusCode();   // HttpStatusCode
        var pd = ex.getBody();                 // ProblemDetail (nullable)

        String title = (pd != null && pd.getTitle() != null) ? pd.getTitle() : ex.getReason();

        var body = build(
                req,
                HttpStatus.valueOf(statusCode.value()),
                (title != null && !title.isBlank()) ? title : "요청 처리 중 오류가 발생했습니다.",
                "RESPONSE_STATUS",
                null
        );
        return ResponseEntity.status(statusCode).body(body);
    }

    /**
     * 사용자 ID 중복 예외 → 409 Conflict
     */
    @ExceptionHandler(DuplicateIdException.class)
    public ResponseEntity<ApiError> handleDuplicateId(DuplicateIdException ex, HttpServletRequest req) {
        ApiError error = new ApiError(
                OffsetDateTime.now(),
                req.getRequestURI(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                "DUPLICATE_ID",         // 비즈니스 에러 코드
                MDC.get("traceId"),
                null
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    // 그 외 처리되지 않은 예외
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleOthers(Exception ex, HttpServletRequest req) {
        return build(req, HttpStatus.INTERNAL_SERVER_ERROR, "요청 처리 중 오류가 발생했습니다.", "INTERNAL", null);
    }

    // 공통 빌더
    private ApiError build(HttpServletRequest req, HttpStatus status, String message, String code,
                           List<ApiError.FieldError> details) {
        return new ApiError(
                OffsetDateTime.now(),
                req.getRequestURI(),
                status.value(),
                status.getReasonPhrase(),
                message,
                code,
                MDC.get("traceId"), // MDC 필터를 두었다면 연동
                details
        );
    }
}
