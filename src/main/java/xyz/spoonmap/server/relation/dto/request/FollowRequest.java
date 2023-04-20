package xyz.spoonmap.server.relation.dto.request;

import javax.validation.constraints.NotNull;

public record FollowRequest(

    @NotNull
    Long targetMemberId

) {
}
