package xyz.spoonmap.server.notification.repository;

import static xyz.spoonmap.server.notification.entity.QNotification.notification;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import xyz.spoonmap.server.notification.dto.response.NotificationResponse;

@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public Slice<NotificationResponse> findNotifications(Long memberId, Long lastNotificationId, int size) {

        List<NotificationResponse> notifications = query
            .select(Projections.constructor(
                NotificationResponse.class,
                notification.id, notification.targetId, notification.type,
                notification.createdAt, notification.checked
            ))
            .from(notification)
            .where(
                ltNotificationId(lastNotificationId),
                notification.member.id.eq(memberId)
            )
            .orderBy(notification.id.desc())
            .limit(size)
            .fetch();

        return checkLastPage(notifications, size);
    }

    private BooleanExpression ltNotificationId(Long id) {
        return id == null ? null : notification.id.lt(id);
    }

    private Slice<NotificationResponse> checkLastPage(List<NotificationResponse> notifications, int size) {

        boolean hasNext = false;

        if (notifications.size() > size) {
            hasNext = true;
            notifications.remove(size);
        }

        return new SliceImpl<>(notifications, PageRequest.of(0, size), hasNext);
    }

}
