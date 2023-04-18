package xyz.spoonmap.server.spoon.dto;

import xyz.spoonmap.server.member.dto.response.MemberResponse;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.spoon.entity.Spoon;

import java.time.LocalDateTime;

public record SpoonResponseDto(
        Long postId,
        MemberResponse memberResponse,
        LocalDateTime createdAt
) {
    public SpoonResponseDto(Spoon spoon, Member member) {
        this(spoon.getId().getPostNo(),
                new MemberResponse(member),
                spoon.getCreatedAt());
    }
}
