package xyz.spoonmap.server.notification.event.eventlistener;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import xyz.spoonmap.server.member.repository.MemberRepository;
import xyz.spoonmap.server.notification.entity.Notification;
import xyz.spoonmap.server.notification.event.NotificationEvent;
import xyz.spoonmap.server.notification.repository.NotificationRepository;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void eventListener(NotificationEvent event) {

        notificationRepository.save(new Notification(event.getMember(), event.getNotificationType(), event.getTargetId()));
    }

}
