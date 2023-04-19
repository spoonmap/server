package xyz.spoonmap.server.notification.entity;

import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.notification.entity.enums.NotificationType;

@Entity
@Table(name = "notifications")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_no")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_no")
    @NotNull
    private Member member;

    @Column(name = "target_no")
    private Long targetId;

    @CreatedDate
    @Column(name = "created_at")
    @NotNull
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @NotNull
    private NotificationType type;

    @NotNull
    private Boolean checked;

    private Notification(Member member, Long targetId, NotificationType type, Boolean checked) {
        this.member = member;
        this.targetId = targetId;
        this.type = type;
        this.checked = checked;
    }

    public static Notification comment(Member postAuthor, Long commentId) {
        return new Notification(postAuthor, commentId, NotificationType.COMMENT, false);
    }

    public static Notification follow(Member followReceiver, Long followSenderId) {
        return new Notification(followReceiver, followSenderId, NotificationType.FOLLOW, false);
    }

}
