package xyz.spoonmap.server.notification.dto.response;

import java.time.LocalDateTime;
import xyz.spoonmap.server.notification.entity.enums.NotificationType;

public record NotificationResponse(
    Long id,
    Long targetId,
    NotificationType notificationType,
    LocalDateTime createdAt,
    boolean checked
) {
}
