package xyz.spoonmap.server.notification.repository;

import org.springframework.data.domain.Slice;
import xyz.spoonmap.server.notification.dto.response.NotificationResponse;

public interface NotificationRepositoryCustom {

    Slice<NotificationResponse> findNotifications(Long memberId, Long lastNotificationId, int size);

}
