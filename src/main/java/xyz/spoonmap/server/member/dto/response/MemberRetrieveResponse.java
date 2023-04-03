package xyz.spoonmap.server.member.dto.response;

import java.util.List;

public record MemberRetrieveResponse(
    List<MemberResponse> members
) {
}
