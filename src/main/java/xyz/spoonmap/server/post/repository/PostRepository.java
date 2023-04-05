package xyz.spoonmap.server.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.spoonmap.server.post.entity.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    public List<Post> findByDeletedAtIsNull();

    public Optional<Post> findPostByIdAndDeletedAtIsNull(Long id);
}
