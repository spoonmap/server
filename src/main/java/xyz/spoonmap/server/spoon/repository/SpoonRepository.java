package xyz.spoonmap.server.spoon.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import xyz.spoonmap.server.spoon.entity.Spoon;

public interface SpoonRepository extends JpaRepository<Spoon, Spoon.Pk> {
    Slice<Spoon> findAllByPostId(Long postId, Pageable pageable);

    Long countSpoonsByPostId(Long postId);
}
