package xyz.spoonmap.server.relation.repository;

import java.util.List;
import xyz.spoonmap.server.member.entity.Member;

public interface RelationRepositoryCustom {

    List<Member> findFollowers(Long id);

    List<Member> findFollows(Long id);

    List<Member> findMyFollowRequest(Long id);

    List<Member> findMyFollowerRequest(Long id);

}
