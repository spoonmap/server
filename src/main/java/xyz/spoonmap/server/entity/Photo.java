package xyz.spoonmap.server.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "photos")
@Entity
@NoArgsConstructor
@Getter
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id", nullable = false)
    private Long id;

    @Column(name = "origin_name", nullable = false)
    private String originName;

    @Column(nullable = false)
    private String url;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "deleted_at", nullable = false)
    private LocalDateTime deletedAt;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private Photo(Long id, String originName, String url, LocalDateTime createdAt, LocalDateTime deletedAt, Post post) {
        this.id = id;
        this.originName = originName;
        this.url = url;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
        this.post = post;
    }

    public static Photo of(String originName, String url, Post post) {
        return new Photo(null, originName, url, LocalDateTime.now(), null, post);
    }

}
