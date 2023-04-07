package xyz.spoonmap.server.restaurant.dto.request;

import xyz.spoonmap.server.restaurant.entity.Restaurant;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record RestaurantRequestDto(
        @NotNull(message = "식당 이름이 비어있습니다.")
        @Size(min = 1, max = 20, message = "식당 이름은 1~20 글자입니다.")
        String name,

        @NotNull(message = "식당 주소가 필요합니다.")
        @Size(min = 1, max = 50, message = "식당 주소는 1~50 글자입니다.")
        String address,

        @NotNull(message = "위치 정보가 필요합니다. (x)")
        Float x,

        @NotNull(message = "위치 정보가 필요합니다. (y)")
        Float y
) {

    public Restaurant toEntity() {
        return Restaurant.builder()
                         .name(name)
                         .address(address)
                         .x(x)
                         .y(y)
                         .build();
    }
}
