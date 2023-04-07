package xyz.spoonmap.server.dto.response;

public record ErrorResponse(
    Integer code,
    String message
) {}
