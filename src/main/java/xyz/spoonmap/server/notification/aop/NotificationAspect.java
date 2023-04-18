package xyz.spoonmap.server.notification.aop;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import xyz.spoonmap.server.comment.dto.response.CommentResponseDto;
import xyz.spoonmap.server.exception.domain.member.MemberNotFoundException;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.member.repository.MemberRepository;
import xyz.spoonmap.server.notification.entity.enums.NotificationType;
import xyz.spoonmap.server.notification.event.NotificationEvent;
import xyz.spoonmap.server.relation.dto.response.FollowAddResponse;

@Aspect
@Component
@RequiredArgsConstructor
public class NotificationAspect {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final MemberRepository memberRepository;

    @AfterReturning(value = "execution(* xyz.spoonmap.server.comment.service.*.create(..))", returning = "returnValue")
    public void addCommentNotification(CommentResponseDto returnValue) {
        Long commentWriterId = returnValue.memberResponse().id();
        Member postAuthor = memberRepository.findById(returnValue.authorId())
                                            .orElseThrow(MemberNotFoundException::new);

        if (Objects.equals(commentWriterId, postAuthor.getId())) {
            return;
        }

        applicationEventPublisher
            .publishEvent(new NotificationEvent(postAuthor, NotificationType.COMMENT, commentWriterId));
    }

    @AfterReturning(value = "execution(* xyz.spoonmap.server.relation.service.*.requestFollow(..))", returning = "returnValue")
    public void addFollowNotification(FollowAddResponse returnValue) {
        Member followReceiver = memberRepository.findById(returnValue.receiverId())
                                                .orElseThrow(MemberNotFoundException::new);

        if (Objects.equals(followReceiver.getId(), returnValue.senderId())) {
            return;
        }

        applicationEventPublisher
            .publishEvent(new NotificationEvent(followReceiver, NotificationType.COMMENT, returnValue.senderId()));
    }

}
