package xyz.spoonmap.server.post.dto.request;

import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import xyz.spoonmap.server.photo.dto.request.PhotoRequestDto;
import xyz.spoonmap.server.post.entity.enums.MealTime;
import xyz.spoonmap.server.restaurant.dto.request.RestaurantRequestDto;

public record PostRequestDto(
        @NotNull(message = "식당 정보가 비어있습니다.")
        RestaurantRequestDto restaurant,

        List<PhotoRequestDto> photos,

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

}
