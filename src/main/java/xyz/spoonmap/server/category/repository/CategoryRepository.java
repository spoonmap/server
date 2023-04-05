package xyz.spoonmap.server.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.spoonmap.server.category.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
