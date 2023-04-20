package xyz.spoonmap.server.restaurant.dto.response;

import xyz.spoonmap.server.restaurant.entity.Restaurant;

public record RestaurantResponseDto(
        String name,
        String address,
        Float x,
        Float y
) {
    public RestaurantResponseDto(Restaurant restaurant) {
        this(restaurant.getName(), restaurant.getAddress(), restaurant.getX(), restaurant.getY());
    }
}
