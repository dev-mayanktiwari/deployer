package tech.mayanktiwari.deployer.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import tech.mayanktiwari.deployer.common.exception.ErrorCode;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericApiResponse<T> {

    boolean success;
    String message;
    T data;
    String errorCode;
    Instant timestamp;
    String path;
    List<FieldValidationError> details;

    // ── Success factories ──────────────────────────────────────────────────────

    public static <T> GenericApiResponse<T> success(T data) {
        return GenericApiResponse.<T>builder()
                .success(true)
                .data(data)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> GenericApiResponse<T> success(String message, T data) {
        return GenericApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(Instant.now())
                .build();
    }

    public static GenericApiResponse<Void> success(String message) {
        return GenericApiResponse.<Void>builder()
                .success(true)
                .message(message)
                .timestamp(Instant.now())
                .build();
    }

    // ── Failure factories ──────────────────────────────────────────────────────

    public static <T> GenericApiResponse<T> failure(ErrorCode errorCode, String path) {
        return GenericApiResponse.<T>builder()
                .success(false)
                .message(errorCode.getDefaultMessage())
                .errorCode(errorCode.name())
                .timestamp(Instant.now())
                .path(path)
                .build();
    }

    public static <T> GenericApiResponse<T> failure(String message, ErrorCode errorCode, String path) {
        return GenericApiResponse.<T>builder()
                .success(false)
                .message(message)
                .errorCode(errorCode.name())
                .timestamp(Instant.now())
                .path(path)
                .build();
    }

    public static <T> GenericApiResponse<T> failure(String message, ErrorCode errorCode, String path, List<FieldValidationError> details) {
        return GenericApiResponse.<T>builder()
                .success(false)
                .message(message)
                .errorCode(errorCode.name())
                .timestamp(Instant.now())
                .path(path)
                .details(details)
                .build();
    }
}
