package xyz.spoonmap.server.post.entity;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import xyz.spoonmap.server.category.entity.Category;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.post.entity.enums.MealTime;
import xyz.spoonmap.server.restaurant.entity.Restaurant;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

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

    @Size(max = 1000)
    private String content;

    @Column(name = "meal_time")
    @Enumerated(EnumType.STRING)
    @NotNull
    private MealTime mealTime;

    @NotNull
    @Max(value = 10)
    @PositiveOrZero
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

    @Builder
    public Post(Member member, Restaurant restaurant, Category category, String title, String content, MealTime mealTime, Byte starRating) {
        this.member = member;
        this.restaurant = restaurant;
        this.category = category;
        this.title = title;
        this.content = content;
        this.mealTime = mealTime;
        this.starRating = starRating;
        this.createdAt = LocalDateTime.now();
    }

    public void update(Restaurant restaurant, Category category, String title, String content, MealTime mealTime, Byte starRating) {
        this.restaurant = restaurant;
        this.category = category;
        this.title = title;
        this.content = content;
        this.mealTime = mealTime;
        this.starRating = starRating;
        this.modifiedAt = LocalDateTime.now();
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }
}

