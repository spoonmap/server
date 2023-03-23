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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_no")
    @NotNull
    private Member member;

    @OneToOne
    @JoinColumn(name = "restaurant_no")
    @NotNull
    private Restaurant restaurant;

    @OneToOne
    @JoinColumn(name = "category_no")
    @NotNull
    private Category category;

    @NotNull
    @Size(min = 1, max = 40)
    private String title;

    @Size(min = 1, max = 1000)
    private String content;

    @Column(name = "meal_time")
    @Enumerated(EnumType.STRING)
    @NotNull
    @Size(min = 1, max = 2)
    private MealTime mealTime;

    @NotNull
    private Byte starRating;

    @CreatedDate
    @Column(name = "created_at")
    @NotNull
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public Post(Member member, Restaurant restaurant, Category category, String title, String content,
            MealTime mealTime,
            Byte starRating) {
        this.member = member;
        this.restaurant = restaurant;
        this.category = category;
        this.title = title;
        this.content = content;
        this.mealTime = mealTime;
        this.starRating = starRating;
    }
}

