package xyz.spoonmap.server.post.entity;


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
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import xyz.spoonmap.server.category.entity.Category;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.post.entity.enums.MealTime;
import xyz.spoonmap.server.restaurant.entity.Restaurant;

@Entity
@Table(name = "posts")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
public class Post {

    @Id
    @Column(name = "post_no")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_no")
    private Member member;

    @OneToOne
    @JoinColumn(name = "restaurant_no")
    private Restaurant restaurant;

    @OneToOne
    @JoinColumn(name = "category_no")
    private Category category;

    @Column(nullable = false, length = 40)
    private String title;

    @Column(length = 1000)
    private String content;

    @Column(name = "meal_time", nullable = false)
    @Enumerated(EnumType.STRING)
    private MealTime mealTime;

    @Column(nullable = false)
    private Byte starRating;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public Post(Member member, Restaurant restaurant, Category category, String title, String content, MealTime mealTime,
            Byte starRating) {
        this.member = member;
        this.restaurant = restaurant;
        this.category = category;
        this.title = title;
        this.content = content;
        this.mealTime = mealTime;
        this.starRating = starRating;
        this.createdAt = LocalDateTime.now();
    }
}

