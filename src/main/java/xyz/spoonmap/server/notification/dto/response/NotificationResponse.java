package xyz.spoonmap.server.notification.dto.response;

import java.time.LocalDateTime;
import lombok.Getter;
import xyz.spoonmap.server.notification.entity.enums.NotificationType;

@Getter
public class NotificationResponse {

    private final Long id;
    private final Long targetId;
    private final NotificationType notificationType;
    private final LocalDateTime createdAt;
    private final boolean checked;

    public NotificationResponse(Long id, Long commentId, Long memberId, NotificationType notificationType,
                                LocalDateTime createdAt, boolean checked) {

        this.id = id;
        this.targetId = notificationType == NotificationType.COMMENT ? commentId : memberId;
        this.notificationType = notificationType;
        this.createdAt = createdAt;
        this.checked = checked;
    }

}
