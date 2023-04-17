package xyz.spoonmap.server.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public record ErrorResponse(
    Integer code,
    String message
) {

    public static ResponseEntity<ErrorResponse> fail(HttpStatus status, Integer code, String message) {
        return ResponseEntity.status(status)
                             .body(new ErrorResponse(code, message));
    }

    public static ErrorResponse fail(Integer code, String message) {
        return new ErrorResponse(code, message);
    }

}
