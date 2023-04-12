package xyz.spoonmap.server.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public record Response<T>(
    Integer code,
    T data
) {

    public static <T> Response<T> of(Integer code, T data) {
        return new Response<>(code, data);
    }

    public static <T> ResponseEntity<Response<T>> success(HttpStatus httpStatus, Integer code, T data) {
        return ResponseEntity.status(httpStatus).body(Response.of(code, data));
    }

}
