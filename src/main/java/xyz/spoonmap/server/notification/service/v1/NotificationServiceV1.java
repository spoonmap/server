package xyz.spoonmap.server.notification.service.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.spoonmap.server.authentication.CustomUserDetail;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.notification.dto.response.NotificationResponse;
import xyz.spoonmap.server.notification.dto.response.SliceResponse;
import xyz.spoonmap.server.notification.repository.NotificationRepository;
import xyz.spoonmap.server.notification.service.NotificationService;

@Service
@RequiredArgsConstructor
public class NotificationServiceV1 implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    @Override
    public SliceResponse<NotificationResponse> retrieveNotification(final UserDetails userDetails,
                                                                    final Long lastNotificationId,
                                                                    final int size) {

        Member member = ((CustomUserDetail) userDetails).getMember();

        Slice<NotificationResponse> notifications =
            notificationRepository.findNotifications(member.getId(), lastNotificationId, size);

        return new SliceResponse<>(notifications.hasNext(), notifications.getContent());
    }

}
