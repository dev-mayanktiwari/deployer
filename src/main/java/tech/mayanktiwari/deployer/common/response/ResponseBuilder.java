package tech.mayanktiwari.deployer.common.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class ResponseBuilder {

    private ResponseBuilder() {}

    // 200 — data only
    public static <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return ResponseEntity.ok(ApiResponse.of(data));
    }

    // 200 — data + message
    public static <T> ResponseEntity<ApiResponse<T>> ok(T data, String message) {
        return ResponseEntity.ok(ApiResponse.of(data, message));
    }

    // 200 — message only, no data (e.g. logout, soft-delete confirmations)
    public static ResponseEntity<ApiResponse<Void>> ok(String message) {
        return ResponseEntity.ok(ApiResponse.message(message));
    }

    // 201 — data only
    public static <T> ResponseEntity<ApiResponse<T>> created(T data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(data));
    }

    // 201 — data + message
    public static <T> ResponseEntity<ApiResponse<T>> created(T data, String message) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(data, message));
    }

    // 204 — no content (hard deletes, fire-and-forget)
    public static ResponseEntity<Void> noContent() {
        return ResponseEntity.noContent().build();
    }
}
