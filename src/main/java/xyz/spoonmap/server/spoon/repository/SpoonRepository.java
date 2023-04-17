package xyz.spoonmap.server.spoon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.spoonmap.server.spoon.entity.Spoon;

import java.util.List;

public interface SpoonRepository extends JpaRepository<Spoon, Spoon.Pk> {
    List<Spoon> findAllByPostId(Long postId);
}
