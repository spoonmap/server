package xyz.spoonmap.server.relation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.spoonmap.server.relation.entity.Relation;

public interface RelationRepository extends JpaRepository<Relation, Relation.Pk>, RelationRepositoryCustom {
}
