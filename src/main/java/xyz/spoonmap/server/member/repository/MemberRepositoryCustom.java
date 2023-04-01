package xyz.spoonmap.server.member.repository;

import java.util.List;
import xyz.spoonmap.server.member.dto.response.MemberResponse;

public interface MemberRepositoryCustom {

    List<MemberResponse> findMembersByNickname(String nickname);

}
