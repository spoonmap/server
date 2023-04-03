package xyz.spoonmap.server.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.spoonmap.server.post.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

}
