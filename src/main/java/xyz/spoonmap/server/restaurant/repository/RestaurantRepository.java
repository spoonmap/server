package xyz.spoonmap.server.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.spoonmap.server.restaurant.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

}
