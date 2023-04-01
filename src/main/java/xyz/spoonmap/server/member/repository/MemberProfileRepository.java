package xyz.spoonmap.server.member.repository;

import java.util.Optional;
import xyz.spoonmap.server.member.entity.Member;

public interface MemberProfileRepository {

    Optional<Member> findByNickname(String nickname);

}
