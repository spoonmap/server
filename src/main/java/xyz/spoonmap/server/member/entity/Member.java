package xyz.spoonmap.server.member.entity;

import static xyz.spoonmap.server.member.enums.VerifyStatus.*;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import xyz.spoonmap.server.member.enums.VerifyStatus;

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

    @Size(min = 2, max = 10)
    @NotNull
    @Column
    private String name;

    @Size(max = 500)
    @Email
    @NotNull
    private String email;

    @Size(min = 50, max = 60)
    @NotNull
    private String password;

    @Size(min = 2, max = 20)
    @NotNull
    private String nickname;

    @Size(min = 1, max = 500)
    private String avatar;

    @NotNull
    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "verify_status")
    @Enumerated(EnumType.STRING)
    private VerifyStatus verifyStatus;

    @Builder
    public Member(String name, String email, String password, String nickname, String avatar) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.avatar = avatar;
        this.verifyStatus = SIGNUP;
    }

    public void withdraw() {
        this.deletedAt = LocalDateTime.now();
    }

    public void verify() {
        this.verifyStatus = VERIFIED;
    }

    public void updatePassword(final String password) {
        this.password = password;
    }

}
