package xyz.spoonmap.server.post.dto.response;

import xyz.spoonmap.server.category.entity.Category;
import xyz.spoonmap.server.post.entity.Post;
import xyz.spoonmap.server.post.entity.enums.MealTime;
import xyz.spoonmap.server.restaurant.dto.response.RestaurantResponseDto;

import java.time.LocalDateTime;

public record PostResponseDto(
        Long id,
        Long authorId,
        String title,
        String[] photoUrls,
        RestaurantResponseDto restaurant,
        Category category,
        Byte starRating,
        String content,
        MealTime mealTime,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {

    public PostResponseDto(Post post, String[] urls) {
        this(post.getId(),
                post.getMember().getId(),
                post.getTitle(),
                urls,
                new RestaurantResponseDto(
                        post.getRestaurant().getName(),
                        post.getRestaurant().getAddress(),
                        post.getRestaurant().getX(),
                        post.getRestaurant().getY()),
                post.getCategory(),
                post.getStarRating(),
                post.getContent(),
                post.getMealTime(),
                post.getCreatedAt(),
                post.getModifiedAt());
    }
}
