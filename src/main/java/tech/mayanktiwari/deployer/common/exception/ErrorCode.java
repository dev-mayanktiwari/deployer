package tech.mayanktiwari.deployer.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

  // Auth
  UNAUTHORIZED("Authentication is required to access this resource", HttpStatus.UNAUTHORIZED),
  FORBIDDEN("You do not have permission to perform this action", HttpStatus.FORBIDDEN),
  TOKEN_INVALID("The provided token is invalid or has expired", HttpStatus.UNAUTHORIZED),

  // Resources
  RESOURCE_NOT_FOUND("The requested resource was not found", HttpStatus.NOT_FOUND),
  RESOURCE_ALREADY_EXISTS(
      "A resource with the provided details already exists", HttpStatus.CONFLICT),

  // Request
  VALIDATION_FAILED("Request validation failed", HttpStatus.BAD_REQUEST),
  BAD_REQUEST(
      "The request could not be understood or was missing required parameters",
      HttpStatus.BAD_REQUEST),
  METHOD_NOT_SUPPORTED(
      "HTTP method not supported for this endpoint", HttpStatus.METHOD_NOT_ALLOWED),

  // Server
  INTERNAL_ERROR(
      "An unexpected error occurred. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);

  private final String defaultMessage;
  private final HttpStatus httpStatus;

  ErrorCode(String defaultMessage, HttpStatus httpStatus) {
    this.defaultMessage = defaultMessage;
    this.httpStatus = httpStatus;
  }
}
