package tech.mayanktiwari.deployer.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.util.List;
import tech.mayanktiwari.deployer.common.exception.ErrorCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(
    String code,
    String message,
    String path,
    String timestamp,
    List<FieldValidationError> details) {

  public static ApiError of(ErrorCode errorCode, String path) {
    return new ApiError(
        errorCode.name(), errorCode.getDefaultMessage(), path, Instant.now().toString(), null);
  }

  public static ApiError of(ErrorCode errorCode, String message, String path) {
    return new ApiError(errorCode.name(), message, path, Instant.now().toString(), null);
  }

  public static ApiError of(
      ErrorCode errorCode, String message, String path, List<FieldValidationError> details) {
    return new ApiError(errorCode.name(), message, path, Instant.now().toString(), details);
  }
}
