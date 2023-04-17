package xyz.spoonmap.server.notification.event;

import lombok.Getter;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.notification.entity.enums.NotificationType;

@Getter
public class NotificationEvent {

    private final Member member;
    private final NotificationType notificationType;
    private final Long targetId;

    public NotificationEvent(Member member, NotificationType notificationType, Long targetId) {
        this.member = member;
        this.notificationType = notificationType;
        this.targetId = targetId;
    }

}
