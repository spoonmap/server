package xyz.spoonmap.server.notification.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import xyz.spoonmap.server.comment.dto.response.CommentResponseDto;
import xyz.spoonmap.server.notification.entity.enums.NotificationType;
import xyz.spoonmap.server.notification.event.NotificationEvent;
import xyz.spoonmap.server.relation.dto.response.FollowAddResponse;

@Aspect
@Component
@RequiredArgsConstructor
public class NotificationAspect {

    private final ApplicationEventPublisher applicationEventPublisher;

    @AfterReturning(value = "execution(* xyz.spoonmap.server.comment.service.*.create(..))", returning = "commentResponse")
    public void addCommentNotification(CommentResponseDto commentResponse) {

        applicationEventPublisher
            .publishEvent(
                new NotificationEvent(commentResponse.authorId(), commentResponse.id(), NotificationType.COMMENT));
    }

    @AfterReturning(value = "execution(* xyz.spoonmap.server.relation.service.*.requestFollow(..))", returning = "followResponse")
    public void addFollowNotification(FollowAddResponse followResponse) {

        applicationEventPublisher
            .publishEvent(
                new NotificationEvent(followResponse.receiverId(), followResponse.senderId(), NotificationType.FOLLOW));
    }

}
