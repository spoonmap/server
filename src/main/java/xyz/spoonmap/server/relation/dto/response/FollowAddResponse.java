package xyz.spoonmap.server.relation.dto.response;

public record FollowAddResponse(
    Long senderId,
    Long receiverId
) {
}
