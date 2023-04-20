package xyz.spoonmap.server.comment.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.Optional;
import xyz.spoonmap.server.comment.entity.Comment;
import xyz.spoonmap.server.member.dto.response.MemberResponse;

public record CommentResponseDto(
    Long id,
    MemberResponse memberResponse,
    Long parentCommentId,
    String content,
    LocalDateTime createdAt,
    LocalDateTime modifiedAt,
    @JsonIgnore
    Long authorId
) {
    public CommentResponseDto(Comment comment) {
        this(comment.getId(),
            new MemberResponse(comment.getMember()),
            Optional.ofNullable(comment.getParentComment())
                    .map(Comment::getId)
                    .orElse(null),
            comment.getContent(),
            comment.getCreatedAt(),
            comment.getModifiedAt(),
            null);
    }

    public CommentResponseDto(Comment comment, Long authorId) {
        this(comment.getId(),
            new MemberResponse(comment.getMember()),
            Optional.ofNullable(comment.getParentComment())
                    .map(Comment::getId)
                    .orElse(null),
            comment.getContent(),
            comment.getCreatedAt(),
            comment.getModifiedAt(),
            authorId);
    }
}
