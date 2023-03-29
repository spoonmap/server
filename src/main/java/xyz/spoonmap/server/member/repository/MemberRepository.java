package xyz.spoonmap.server.member.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import xyz.spoonmap.server.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

}
