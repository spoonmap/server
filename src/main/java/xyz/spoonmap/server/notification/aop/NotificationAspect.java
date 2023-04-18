package xyz.spoonmap.server.notification.aop;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import xyz.spoonmap.server.authentication.CustomUserDetail;
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
        Member commentWriter = this.getMember();
        Member author = memberRepository.findById(returnValue.authorId())
                                        .orElseThrow(MemberNotFoundException::new);

        if (Objects.equals(commentWriter.getId(), author.getId())) {
            return;
        }

        applicationEventPublisher
            .publishEvent(new NotificationEvent(author, NotificationType.COMMENT, commentWriter.getId()));
    }

    @AfterReturning(value = "execution(* xyz.spoonmap.server.relation.service.*.requestFollow(..))", returning = "returnValue")
    public void addFollowNotification(FollowAddResponse returnValue) {
        Member member = this.getMember();
        if (Objects.equals(member.getId(), returnValue.senderId())) {
            return;
        }
        applicationEventPublisher
            .publishEvent(new NotificationEvent(member, NotificationType.COMMENT, returnValue.senderId()));
    }

    private Member getMember() {
        return ((CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMember();
    }

}
