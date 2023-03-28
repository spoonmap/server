package xyz.spoonmap.server.dto.response;

public record Response<T>(
    Integer code,
    T data
) {

    public static <T> Response<T> of(Integer code, T data) {
        return new Response<>(code, data);
    }

}
