package xyz.spoonmap.server.photo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.spoonmap.server.photo.entity.Photo;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
}
