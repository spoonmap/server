package xyz.spoonmap.server.relation.dto.response;

import java.util.List;
import xyz.spoonmap.server.member.dto.response.MemberResponse;

public record FollowResponse(
    Long id,
    List<MemberResponse> followers
) {
}
