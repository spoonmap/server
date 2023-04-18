package xyz.spoonmap.server.notification.service.v1;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.SliceImpl;
import org.springframework.security.core.userdetails.UserDetails;
import xyz.spoonmap.server.authentication.CustomUserDetail;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.notification.dto.response.NotificationResponse;
import xyz.spoonmap.server.notification.dto.response.SliceResponse;
import xyz.spoonmap.server.notification.entity.enums.NotificationType;
import xyz.spoonmap.server.notification.repository.NotificationRepository;
import xyz.spoonmap.server.util.RandomEntityGenerator;

@ExtendWith(MockitoExtension.class)
class NotificationServiceV1Test {

    @InjectMocks
    NotificationServiceV1 notificationServiceV1;

    @Mock
    NotificationRepository notificationRepository;

    @Test
    @DisplayName("알림 조회")
    void testRetrieveNotification() {
        Long id = 1L;
        Long last = 10L;
        int size = 10;
        Member member = mock(Member.class);
        UserDetails userDetails = new CustomUserDetail(member);
        List<NotificationResponse> notificationResponses = IntStream.range(0, size).mapToObj(i -> generate()).toList();
        SliceImpl<NotificationResponse> notifications = new SliceImpl<>(notificationResponses);

        given(member.getId()).willReturn(id);
        given(notificationRepository.findNotifications(id, last, size)).willReturn(notifications);

        SliceResponse<NotificationResponse> response =
            notificationServiceV1.retrieveNotification(userDetails, last, size);

        assertThat(response.hasNext()).isFalse();
        assertThat(response.content()).hasSize(size);

        then(notificationRepository).should(times(1)).findNotifications(id, last, size);
    }

    private static NotificationResponse generate() {
        return convert(RandomEntityGenerator.create(DummyResponse.class));
    }

    static class DummyResponse {
        Long id;
        Long targetId;
        NotificationType notificationType;
        LocalDateTime createdAt;
    }

    private static NotificationResponse convert(DummyResponse dummy) {
        return new NotificationResponse(dummy.id, dummy.targetId, dummy.notificationType, dummy.createdAt, false);
    }

}
