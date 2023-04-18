package xyz.spoonmap.server.notification.dto.response;

import java.util.List;

public record SliceResponse<T>(
    boolean hasNext,
    List<T> content
) {
}
