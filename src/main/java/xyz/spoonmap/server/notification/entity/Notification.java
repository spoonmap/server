package xyz.spoonmap.server.notification.entity;


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
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.notification.entity.enums.NotificationType;

@Entity
@Table(name = "notifications")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_no")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_no")
    private Member member;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean checked;

    public Notification(Member member, NotificationType type) {
        this.member = member;
        this.type = type;
    }
}
