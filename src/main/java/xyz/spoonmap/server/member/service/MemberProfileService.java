package xyz.spoonmap.server.member.service;

import xyz.spoonmap.server.member.dto.response.MemberResponse;

public interface MemberProfileService {

    MemberResponse retrieveByNickname(String nickname);

}
