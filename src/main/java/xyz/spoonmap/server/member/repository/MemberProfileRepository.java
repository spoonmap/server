package xyz.spoonmap.server.member.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import xyz.spoonmap.server.member.entity.Member;

public interface MemberProfileRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByNickname(String nickname);

}
