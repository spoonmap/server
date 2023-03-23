package xyz.spoonmap.server.member.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "members")
@Entity
@NoArgsConstructor
@Getter
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

    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @Column(name="deleted_at")
    private LocalDateTime deletedAt;

}
