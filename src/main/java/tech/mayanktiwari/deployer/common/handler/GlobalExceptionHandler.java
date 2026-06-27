package tech.mayanktiwari.deployer.common.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
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
import tech.mayanktiwari.deployer.common.response.ApiError;
import tech.mayanktiwari.deployer.common.response.FieldValidationError;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(AppException.class)
  public ResponseEntity<ApiError> handleAppException(AppException ex, HttpServletRequest request) {
    ErrorCode code = ex.getErrorCode();
    return ResponseEntity.status(code.getHttpStatus())
        .body(ApiError.of(code, ex.getMessage(), request.getRequestURI()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleValidation(
      MethodArgumentNotValidException ex, HttpServletRequest request) {
    List<FieldValidationError> details =
        ex.getBindingResult().getFieldErrors().stream()
            .map(fe -> new FieldValidationError(fe.getField(), fe.getDefaultMessage()))
            .toList();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(
            ApiError.of(
                ErrorCode.VALIDATION_FAILED,
                ErrorCode.VALIDATION_FAILED.getDefaultMessage(),
                request.getRequestURI(),
                details));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ApiError> handleConstraintViolation(
      ConstraintViolationException ex, HttpServletRequest request) {
    List<FieldValidationError> details =
        ex.getConstraintViolations().stream()
            .map(
                cv -> {
                  String field = cv.getPropertyPath().toString();
                  return new FieldValidationError(
                      field.contains(".") ? field.substring(field.lastIndexOf('.') + 1) : field,
                      cv.getMessage());
                })
            .toList();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(
            ApiError.of(
                ErrorCode.VALIDATION_FAILED,
                ErrorCode.VALIDATION_FAILED.getDefaultMessage(),
                request.getRequestURI(),
                details));
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiError> handleUnreadableBody(HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(
            ApiError.of(
                ErrorCode.BAD_REQUEST,
                "Request body is missing or malformed",
                request.getRequestURI()));
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ApiError> handleMissingParam(
      MissingServletRequestParameterException ex, HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(
            ApiError.of(
                ErrorCode.BAD_REQUEST,
                "Required parameter '" + ex.getParameterName() + "' is missing",
                request.getRequestURI()));
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ApiError> handleNotFound(HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(
            ApiError.of(
                ErrorCode.RESOURCE_NOT_FOUND,
                "No endpoint found at " + request.getRequestURI(),
                request.getRequestURI()));
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ApiError> handleMethodNotSupported(
      HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
        .body(
            ApiError.of(ErrorCode.METHOD_NOT_SUPPORTED, ex.getMessage(), request.getRequestURI()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleUnexpected(Exception ex, HttpServletRequest request) {
    log.error("Unhandled exception at {}: {}", request.getRequestURI(), ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(
            ApiError.of(
                ErrorCode.INTERNAL_ERROR,
                ErrorCode.INTERNAL_ERROR.getDefaultMessage(),
                request.getRequestURI()));
  }
}
