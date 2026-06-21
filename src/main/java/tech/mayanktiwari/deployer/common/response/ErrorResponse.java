package tech.mayanktiwari.deployer.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import tech.mayanktiwari.deployer.common.exception.ErrorCode;

import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        String code,
        String message,
        Instant timestamp,
        String path,
        List<FieldValidationError> details
) {

    public static ErrorResponse of(ErrorCode errorCode, String path) {
        return new ErrorResponse(errorCode.name(), errorCode.getDefaultMessage(), Instant.now(), path, null);
    }

    public static ErrorResponse of(ErrorCode errorCode, String message, String path) {
        return new ErrorResponse(errorCode.name(), message, Instant.now(), path, null);
    }

    public static ErrorResponse of(ErrorCode errorCode, String message, String path, List<FieldValidationError> details) {
        return new ErrorResponse(errorCode.name(), message, Instant.now(), path, details);
    }
}
