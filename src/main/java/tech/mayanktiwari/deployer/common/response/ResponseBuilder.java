package tech.mayanktiwari.deployer.common.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tech.mayanktiwari.deployer.common.exception.ErrorCode;

public final class ResponseBuilder {

  private ResponseBuilder() {}

  // ── Success ────────────────────────────────────────────────────────────────

  public static <T> ResponseEntity<GenericApiResponse<T>> buildSuccessResponse(T data) {
    return ResponseEntity.ok(GenericApiResponse.success(data));
  }

  public static <T> ResponseEntity<GenericApiResponse<T>> buildSuccessResponse(
      String message, T data) {
    return ResponseEntity.ok(GenericApiResponse.success(message, data));
  }

  public static <T> ResponseEntity<GenericApiResponse<T>> buildSuccessResponse(
      String message, T data, HttpStatus status) {
    return ResponseEntity.status(status).body(GenericApiResponse.success(message, data));
  }

  public static ResponseEntity<GenericApiResponse<Void>> buildSuccessResponse(String message) {
    return ResponseEntity.ok(GenericApiResponse.success(message));
  }

  // ── Failure ────────────────────────────────────────────────────────────────
  // Prefer throwing AppException subclasses for most errors — the GlobalExceptionHandler
  // will build the response. Use these only for expected business rejections where you need
  // direct control over the response without an exception crossing the call stack.

  public static <T> ResponseEntity<GenericApiResponse<T>> buildFailureResponse(
      ErrorCode errorCode) {
    return ResponseEntity.status(errorCode.getHttpStatus())
        .body(GenericApiResponse.failure(errorCode, null));
  }

  public static <T> ResponseEntity<GenericApiResponse<T>> buildFailureResponse(
      String message, ErrorCode errorCode) {
    return ResponseEntity.status(errorCode.getHttpStatus())
        .body(GenericApiResponse.failure(message, errorCode, null));
  }

  public static <T> ResponseEntity<GenericApiResponse<T>> buildFailureResponse(
      String message, ErrorCode errorCode, HttpStatus status) {
    return ResponseEntity.status(status).body(GenericApiResponse.failure(message, errorCode, null));
  }
}
