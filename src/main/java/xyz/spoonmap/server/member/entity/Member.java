package xyz.spoonmap.server.member.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Table(name = "members")
@Entity
@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_no")
    private Long id;

    @Max(10)
    @Column(nullable = false)
    private String name;

    @Email
    @Column(nullable = false)
    private String email;

    @Max(60)
    @Column(nullable = false)
    private String password;

    @Max(20)
    @Column(nullable = false)
    private String nickname;

    @Max(500)
    private String avatar;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public Member(String name, String email, String password, String nickname, String avatar) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.avatar = avatar;
    }

    public void withdraw() {
        this.deletedAt = LocalDateTime.now();
    }

}
