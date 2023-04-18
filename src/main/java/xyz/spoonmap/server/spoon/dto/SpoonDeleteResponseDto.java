package xyz.spoonmap.server.spoon.dto;

import xyz.spoonmap.server.spoon.entity.Spoon;

public record SpoonDeleteResponseDto(
        Long postId,
        Long memberId
) {
    public SpoonDeleteResponseDto(Spoon.Pk pk) {
        this(pk.getPostNo(), pk.getMemberNo());
    }
}
