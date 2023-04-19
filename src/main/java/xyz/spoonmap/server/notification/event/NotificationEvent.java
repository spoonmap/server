package xyz.spoonmap.server.notification.event;

import lombok.Getter;
import xyz.spoonmap.server.notification.entity.enums.NotificationType;

@Getter
public class NotificationEvent {

    private final Long notificationReceiverId;
    private final Long targetId;
    private final NotificationType notificationType;

    private NotificationEvent(Long notificationReceiverId, Long targetId, NotificationType notificationType) {
        this.notificationReceiverId = notificationReceiverId;
        this.targetId = targetId;   // Comment Id || Follow Sender
        this.notificationType = notificationType;
    }

    public static NotificationEvent comment(Long postAuthor, Long commentId) {
        return new NotificationEvent(postAuthor, commentId, NotificationType.COMMENT);
    }

    public static NotificationEvent follow(Long receiver, Long sender) {
        return new NotificationEvent(receiver, sender, NotificationType.FOLLOW);
    }

}
