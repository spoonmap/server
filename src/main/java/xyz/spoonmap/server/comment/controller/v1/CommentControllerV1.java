package xyz.spoonmap.server.comment.controller.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.spoonmap.server.comment.dto.request.CommentSaveRequestDto;
import xyz.spoonmap.server.comment.dto.request.CommentUpdateRequestDto;
import xyz.spoonmap.server.comment.dto.response.CommentResponseDto;
import xyz.spoonmap.server.comment.service.v1.CommentServiceV1;
import xyz.spoonmap.server.dto.response.Response;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController()
public class CommentControllerV1 {

    private final CommentServiceV1 commentService;

    @GetMapping("v1/posts/{postId}/comments")
    public ResponseEntity<Response<List<CommentResponseDto>>> findAllBy(@PathVariable Long postId) {
        List<CommentResponseDto> commentResponses = commentService.findAllBy(postId);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(Response.of(HttpStatus.OK.value(), commentResponses));
    }

    @PostMapping("v1/posts/{postId}/comments")
    public ResponseEntity<Response<CommentResponseDto>> create(@AuthenticationPrincipal UserDetails userDetails,
                                                               @PathVariable Long postId,
                                                               @RequestBody @Valid CommentSaveRequestDto requestDto) {
        CommentResponseDto response = commentService.create(userDetails, postId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(Response.of(HttpStatus.CREATED.value(), response));

    }

    @PutMapping("v1/comments/{commentId}")
    public ResponseEntity<Response<CommentResponseDto>> update(@AuthenticationPrincipal UserDetails userDetails,
                                                               @PathVariable Long commentId,
                                                               @RequestBody @Valid CommentUpdateRequestDto requestDto) {
        CommentResponseDto response = commentService.update(userDetails, commentId, requestDto);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(Response.of(HttpStatus.OK.value(), response));
    }

    @DeleteMapping("v1/comments/{commentId}")
    public ResponseEntity<Response<Long>> delete(@AuthenticationPrincipal UserDetails userDetails,
                                                 @PathVariable Long commentId) {
        Long deletedCommentId = commentService.delete(userDetails, commentId);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(Response.of(HttpStatus.OK.value(), deletedCommentId));
    }
}
