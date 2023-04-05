package xyz.spoonmap.server.restaurant.dto.response;

public record RestaurantResponseDto (
        String name,
        String address,
        Float x,
        Float y
) {

}
