package xyz.spoonmap.server.comment.dto.response;

import xyz.spoonmap.server.comment.entity.Comment;
import xyz.spoonmap.server.member.dto.response.MemberResponse;

import java.time.LocalDateTime;

public record CommentResponseDto(
        Long id,
        MemberResponse memberResponse,
        Long parentCommentId,
        String content,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    public CommentResponseDto(Comment comment) {
        this(comment.getId(),
                new MemberResponse(comment.getMember()),
                comment.getParentComment() == null ? null : comment.getParentComment().getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getModifiedAt());
    }
}
