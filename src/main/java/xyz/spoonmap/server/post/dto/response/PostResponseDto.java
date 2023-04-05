package xyz.spoonmap.server.post.dto.response;

import xyz.spoonmap.server.post.entity.Post;
import xyz.spoonmap.server.post.entity.enums.MealTime;
import xyz.spoonmap.server.restaurant.dto.response.RestaurantResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public record PostResponseDto(
        Long id,
        Long authorId,
        String title,
        List<String> photoUrls,
        RestaurantResponseDto restaurant,
        String categoryName,
        Byte starRating,
        String content,
        MealTime mealTime,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {

    public PostResponseDto(Post post, List<String> urls) {
        this(post.getId(),
                post.getMember().getId(),
                post.getTitle(),
                urls,
                new RestaurantResponseDto(
                        post.getRestaurant().getName(),
                        post.getRestaurant().getAddress(),
                        post.getRestaurant().getX(),
                        post.getRestaurant().getY()),
                post.getCategory().getName(),
                post.getStarRating(),
                post.getContent(),
                post.getMealTime(),
                post.getCreatedAt(),
                post.getModifiedAt());
    }
}