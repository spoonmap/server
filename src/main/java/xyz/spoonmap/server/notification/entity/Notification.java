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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.Nullable;
import xyz.spoonmap.server.comment.entity.Comment;
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

    @Nullable
    @OneToOne
    @JoinColumn(name = "comment_writer")
    private Member commentWriter;

    @Nullable
    @OneToOne
    @JoinColumn(name = "comment_no")
    private Comment comment;

    @CreatedDate
    @Column(name = "created_at")
    @NotNull
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @NotNull
    private NotificationType type;

    @NotNull
    private Boolean checked;

    private Notification(Member member, @Nullable Member commentWriter, @Nullable Comment comment,
                         NotificationType type, Boolean checked) {
        this.member = member;
        this.commentWriter = commentWriter;
        this.comment = comment;
        this.type = type;
        this.createdAt = LocalDateTime.now();
        this.checked = checked;
    }

    public static Notification comment(Member postAuthor, @NotNull Comment comment) {
        return new Notification(postAuthor, null, comment, NotificationType.COMMENT, false);
    }

    public static Notification follow(Member followReceiver, @NotNull Member followSender) {
        return new Notification(followReceiver, followReceiver, null, NotificationType.FOLLOW, false);
    }

}
