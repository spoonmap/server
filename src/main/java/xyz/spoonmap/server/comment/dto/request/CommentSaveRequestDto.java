package xyz.spoonmap.server.comment.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record CommentSaveRequestDto(
        @NotNull
        Long postId,
        Long parentCommentId,
        @NotNull(message = "내용은 비어있을수 없습니다.")
        @Size(min = 1, max = 500, message = "내용은 1 ~ 500자까지 가능합니다.")
        String content
) {
}
