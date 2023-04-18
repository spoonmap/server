package xyz.spoonmap.server.notification.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Slice;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import xyz.spoonmap.server.config.jpa.QueryDslConfig;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.member.repository.MemberRepository;
import xyz.spoonmap.server.notification.dto.response.NotificationResponse;
import xyz.spoonmap.server.notification.entity.Notification;
import xyz.spoonmap.server.notification.entity.enums.NotificationType;

@DataJpaTest
@Import(QueryDslConfig.class)
class NotificationRepositoryTest {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    MemberRepository memberRepository;

    Random random = new Random();

    @Test
    @DisplayName("알림 조회")
    void testFindNotifications() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        Member member = new Member("김철수", "asdffsd@email.com", encoder.encode("password"), "철수", null);
        memberRepository.save(member);

        int size = random.nextInt(100);

        List<Notification> notifications = notificationRepository.saveAll(createNotification(size, member));
        Notification notification = notifications.get(notifications.size() - 1);
        Slice<NotificationResponse> notifications1 =
            notificationRepository.findNotifications(member.getId(), notification.getId(), 10);
        assertThat(notifications1.hasNext()).isFalse();
    }

    private List<Notification> createNotification(int size, Member member) {
        return IntStream.range(0, size)
                        .mapToObj(
                            i -> new Notification(member, selectType(random.nextInt(size)), random.nextLong(size)))
                        .toList();
    }

    private NotificationType selectType(int n) {
        return n % 2 == 0 ? NotificationType.COMMENT : NotificationType.FOLLOW;
    }

}