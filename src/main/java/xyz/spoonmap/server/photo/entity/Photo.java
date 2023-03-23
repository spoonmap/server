package xyz.spoonmap.server.photo.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import xyz.spoonmap.server.post.entity.Post;

@Table(name = "photos")
@Entity
@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_no")
    private Long id;

    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "origin_name")
    private String originName;

    @NotNull
    @Size(min = 3, max = 500)
    private String url;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public Photo(String originName, String url, Post post) {
        this.originName = originName;
        this.url = url;
        this.createdAt = LocalDateTime.now();
        this.post = post;
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }

}
