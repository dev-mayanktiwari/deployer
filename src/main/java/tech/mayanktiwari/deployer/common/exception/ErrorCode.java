package tech.mayanktiwari.deployer.common.exception;

import java.text.MessageFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  // Auth
  UNAUTHORIZED(1001, "Authentication is required to access this resource", HttpStatus.UNAUTHORIZED),
  FORBIDDEN(1002, "You do not have permission to perform this action", HttpStatus.FORBIDDEN),
  TOKEN_INVALID(1003, "The provided token is invalid or has expired", HttpStatus.UNAUTHORIZED),

  // Resources
  RESOURCE_NOT_FOUND(
      2001, "resource ::: {0} not found with identifier ::: {1}", HttpStatus.NOT_FOUND),
  RESOURCE_ALREADY_EXISTS(
      2002, "resource ::: {0} already exists with identifier ::: {1}", HttpStatus.CONFLICT),

  // Request
  VALIDATION_FAILED(3001, "Request validation failed", HttpStatus.BAD_REQUEST),
  BAD_REQUEST(
      3002,
      "The request could not be understood or was missing required parameters",
      HttpStatus.BAD_REQUEST),
  METHOD_NOT_SUPPORTED(
      3003, "HTTP method not supported for this endpoint", HttpStatus.METHOD_NOT_ALLOWED),

  // GitHub
  GITHUB_REPO_NOT_ACCESSIBLE(
      4001, "GitHub repository {0} is not accessible or does not exist", HttpStatus.BAD_REQUEST),
  GITHUB_TOKEN_INVALID(
      4002, "GitHub access token is invalid or has expired", HttpStatus.UNAUTHORIZED),
  GITHUB_API_ERROR(4003, "GitHub API request failed: {0}", HttpStatus.BAD_GATEWAY),

  // Deployment
  DEPLOYMENT_ALREADY_IN_PROGRESS(
      4101, "Project {0} already has a deployment in progress", HttpStatus.CONFLICT),
  DEPLOYMENT_NOT_CANCELLABLE(
      4102, "Deployment {0} cannot be cancelled in its current state", HttpStatus.BAD_REQUEST),
  BUILD_FAILED(4103, "Deployment {0} failed during build: {1}", HttpStatus.UNPROCESSABLE_ENTITY),

  // Project
  PROJECT_LIMIT_REACHED(
      4201, "User has reached the maximum number of allowed projects", HttpStatus.FORBIDDEN),
  INVALID_BUILD_COMMAND(4202, "Build command {0} is not allowed", HttpStatus.BAD_REQUEST),

  // Server
  INTERNAL_ERROR(
      5001,
      "An unexpected error occurred. Please try again later.",
      HttpStatus.INTERNAL_SERVER_ERROR),

  // Encryption
  INVALID_ENCRYPTION_CONFIGURATION(
      6001, "Invalid encryption configuration", HttpStatus.INTERNAL_SERVER_ERROR),
  ENCRYPTION_FAILED(6002, "Failed to encrypt value", HttpStatus.INTERNAL_SERVER_ERROR),
  DECRYPTION_FAILED(6003, "Failed to decrypt value", HttpStatus.INTERNAL_SERVER_ERROR);

  private final int code;
  private final String defaultMessage;
  private final HttpStatus httpStatus;

  public AppException build(Object... params) {
    String message =
        params.length > 0 ? MessageFormat.format(this.defaultMessage, params) : this.defaultMessage;
    return new AppException(this, message);
  }

  public AppException buildWithCause(Throwable cause, Object... params) {
    String message =
        params.length > 0 ? MessageFormat.format(this.defaultMessage, params) : this.defaultMessage;
    return new AppException(this, message, cause);
  }
}
