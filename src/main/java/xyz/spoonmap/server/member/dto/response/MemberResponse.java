package xyz.spoonmap.server.member.dto.response;

import xyz.spoonmap.server.member.entity.Member;

public record MemberResponse(
    Long id,
    String name,
    String nickname,
    String profileUrl
) {
    public MemberResponse(Member member) {
        this(member.getId(),
                member.getName(),
                member.getNickname(),
                member.getAvatar());
    }
}
