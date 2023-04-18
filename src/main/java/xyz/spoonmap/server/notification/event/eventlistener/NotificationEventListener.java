package xyz.spoonmap.server.notification.event.eventlistener;

import static xyz.spoonmap.server.notification.entity.enums.NotificationType.COMMENT;
import static xyz.spoonmap.server.notification.entity.enums.NotificationType.FOLLOW;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import xyz.spoonmap.server.comment.entity.Comment;
import xyz.spoonmap.server.comment.repository.CommentRepository;
import xyz.spoonmap.server.exception.domain.comment.CommentNotFoundException;
import xyz.spoonmap.server.exception.domain.member.MemberNotFoundException;
import xyz.spoonmap.server.exception.domain.notification.InvalidNotificationTypeException;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.member.repository.MemberRepository;
import xyz.spoonmap.server.notification.entity.Notification;
import xyz.spoonmap.server.notification.event.NotificationEvent;
import xyz.spoonmap.server.notification.repository.NotificationRepository;
import xyz.spoonmap.server.relation.entity.Relation;
import xyz.spoonmap.server.relation.repository.RelationRepository;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final RelationRepository relationRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void eventListener(NotificationEvent event) {
        if (event.getNotificationType() == COMMENT) {
            saveCommentNotification(event);
        } else if (event.getNotificationType() == FOLLOW) {
            saveFollowNotification(event);
        } else {
            throw new InvalidNotificationTypeException();
        }
    }

    private void saveCommentNotification(NotificationEvent event) {
        Long postAuthorId = event.getNotificationReceiverId();
        Long commentId = event.getTargetId();

        Member postAuthor = memberRepository.findById(postAuthorId)
                                            .orElseThrow(MemberNotFoundException::new);
        Comment comment = commentRepository.findById(commentId)
                                           .orElseThrow(CommentNotFoundException::new);

        if (Objects.equals(postAuthor.getId(), comment.getMember().getId())) {
            return;
        }

        notificationRepository.save(Notification.comment(postAuthor, comment));
    }

    private void saveFollowNotification(NotificationEvent event) {
        Long followReceiverId = event.getNotificationReceiverId();
        Long followSenderId = event.getTargetId();

        if (Objects.equals(followReceiverId, followSenderId)
            || relationRepository.findById(new Relation.Pk(followReceiverId, followSenderId)).isPresent()) {
            return;
        }

        Member receiver = memberRepository.findById(followReceiverId)
                                          .orElseThrow(MemberNotFoundException::new);
        Member sender = memberRepository.findById(followSenderId)
                                        .orElseThrow(MemberNotFoundException::new);

        notificationRepository.save(Notification.follow(receiver, sender));
    }

}
