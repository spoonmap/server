package xyz.spoonmap.server.comment.dto.response;

import xyz.spoonmap.server.comment.entity.Comment;
import xyz.spoonmap.server.member.dto.response.MemberResponse;

import java.time.LocalDateTime;
import java.util.Optional;

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
                Optional.ofNullable(comment.getParentComment())
                        .map(Comment::getId)
                        .orElse(null),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getModifiedAt());
    }
}
