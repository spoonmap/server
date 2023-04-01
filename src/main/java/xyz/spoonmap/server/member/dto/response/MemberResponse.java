package xyz.spoonmap.server.member.dto.response;

public record MemberResponse(
    Long id,
    String name,
    String nickname,
    String profileUrl
    ) {
}
