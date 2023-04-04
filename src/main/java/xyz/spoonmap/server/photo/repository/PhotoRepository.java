package xyz.spoonmap.server.photo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.spoonmap.server.photo.entity.Photo;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    List<Photo> findByPostId(Long postId);
}
