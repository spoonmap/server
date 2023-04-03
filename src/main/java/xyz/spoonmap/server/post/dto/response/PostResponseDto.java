package xyz.spoonmap.server.post.dto.response;

import xyz.spoonmap.server.category.entity.Category;
import xyz.spoonmap.server.photo.entity.Photo;
import xyz.spoonmap.server.post.entity.Post;
import xyz.spoonmap.server.post.entity.enums.MealTime;
import xyz.spoonmap.server.restaurant.entity.Restaurant;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

public record PostResponseDto(
        Long id,
        Long authorId,
        String title,
        String[] photoUrls,
        Restaurant restaurant,
        Category category,
        Byte starRating,
        String content,
        MealTime mealTime,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {

    public PostResponseDto(Post post) {
        this(post.getId(),
                post.getMember().getId(),
                post.getTitle(),
                Optional.ofNullable(post.getPhotos())
                        .orElseGet(Collections::emptyList)
                        .stream()
                        .map(Photo::getUrl).toArray(String[]::new),
                post.getRestaurant(),
                post.getCategory(),
                post.getStarRating(),
                post.getContent(),
                post.getMealTime(),
                post.getCreatedAt(),
                post.getModifiedAt());
    }
}
