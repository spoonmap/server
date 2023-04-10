package xyz.spoonmap.server.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.spoonmap.server.comment.entity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    public List<Comment> findCommentsByPostIdAndDeletedAtIsNull(Long postId);
}
