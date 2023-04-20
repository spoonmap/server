package xyz.spoonmap.server.post.dto.request;

import xyz.spoonmap.server.category.entity.Category;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.post.entity.Post;
import xyz.spoonmap.server.post.entity.enums.MealTime;
import xyz.spoonmap.server.restaurant.dto.request.RestaurantRequestDto;
import xyz.spoonmap.server.restaurant.entity.Restaurant;

import javax.validation.constraints.*;

public record PostSaveRequestDto(
        @NotNull(message = "식당 정보가 비어있습니다.")
        RestaurantRequestDto restaurant,

        @NotNull(message = "카테고리가 비어있습니다.")
        Long categoryId,

        @NotEmpty(message = "게시글 제목이 비어있습니다.")
        @Size(min = 1, max = 40, message = "제목은 1 ~ 40의 길이로 작성되어야 합니다.")
        String title,

        @Size(max = 1000, message = "내용은 1000자를 넘을 수 없습니다.")
        String content,

        @NotNull
        MealTime mealTime,

        @NotNull
        @Max(value = 10, message = "별점은 5점을 넘어갈 수 없습니다.")
        @PositiveOrZero(message = "별점은 0이상의 값이어야 합니다.")
        Byte starRating
) {
    public Post toEntity(Member member, Restaurant restaurant, Category category) {
        return Post.builder()
                   .member(member)
                   .restaurant(restaurant)
                   .category(category)
                   .title(title)
                   .content(content)
                   .mealTime(mealTime)
                   .starRating(starRating)
                   .build();
    }
}
