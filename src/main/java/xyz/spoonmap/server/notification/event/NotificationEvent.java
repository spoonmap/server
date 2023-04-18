package xyz.spoonmap.server.notification.event;

import lombok.Getter;
import xyz.spoonmap.server.notification.entity.enums.NotificationType;

@Getter
public class NotificationEvent {

    private final Long notificationReceiverId;
    private final Long targetId;
    private final NotificationType notificationType;

    public NotificationEvent(Long notificationReceiverId, Long targetId, NotificationType notificationType) {
        this.notificationReceiverId = notificationReceiverId;
        this.targetId = targetId;   // Comment Id || Follow Sender
        this.notificationType = notificationType;
    }

}
