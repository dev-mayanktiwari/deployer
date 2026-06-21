package tech.mayanktiwari.deployer.common.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import tech.mayanktiwari.deployer.common.exception.AppException;
import tech.mayanktiwari.deployer.common.exception.ErrorCode;
import tech.mayanktiwari.deployer.common.response.FieldValidationError;
import tech.mayanktiwari.deployer.common.response.GenericApiResponse;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<GenericApiResponse<Void>> handleAppException(AppException ex, HttpServletRequest request) {
        ErrorCode code = ex.getErrorCode();
        return ResponseEntity.status(code.getHttpStatus())
                .body(GenericApiResponse.failure(ex.getMessage(), code, request.getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GenericApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<FieldValidationError> details = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new FieldValidationError(fe.getField(), fe.getDefaultMessage()))
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(GenericApiResponse.failure(ErrorCode.VALIDATION_FAILED.getDefaultMessage(), ErrorCode.VALIDATION_FAILED, request.getRequestURI(), details));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<GenericApiResponse<Void>> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        List<FieldValidationError> details = ex.getConstraintViolations().stream()
                .map(cv -> {
                    String field = cv.getPropertyPath().toString();
                    return new FieldValidationError(
                            field.contains(".") ? field.substring(field.lastIndexOf('.') + 1) : field,
                            cv.getMessage()
                    );
                })
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(GenericApiResponse.failure(ErrorCode.VALIDATION_FAILED.getDefaultMessage(), ErrorCode.VALIDATION_FAILED, request.getRequestURI(), details));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<GenericApiResponse<Void>> handleUnreadableBody(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(GenericApiResponse.failure("Request body is missing or malformed", ErrorCode.BAD_REQUEST, request.getRequestURI()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<GenericApiResponse<Void>> handleMissingParam(MissingServletRequestParameterException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(GenericApiResponse.failure("Required parameter '" + ex.getParameterName() + "' is missing", ErrorCode.BAD_REQUEST, request.getRequestURI()));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<GenericApiResponse<Void>> handleNotFound(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(GenericApiResponse.failure("No endpoint found at " + request.getRequestURI(), ErrorCode.RESOURCE_NOT_FOUND, request.getRequestURI()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<GenericApiResponse<Void>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(GenericApiResponse.failure(ex.getMessage(), ErrorCode.METHOD_NOT_SUPPORTED, request.getRequestURI()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericApiResponse<Void>> handleUnexpected(Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception at {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(GenericApiResponse.failure(ErrorCode.INTERNAL_ERROR, request.getRequestURI()));
    }
}
