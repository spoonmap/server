package xyz.spoonmap.server.comment.service.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import xyz.spoonmap.server.authentication.CustomUserDetail;
import xyz.spoonmap.server.comment.dto.request.CommentSaveRequestDto;
import xyz.spoonmap.server.comment.dto.request.CommentUpdateRequestDto;
import xyz.spoonmap.server.comment.dto.response.CommentResponseDto;
import xyz.spoonmap.server.comment.entity.Comment;
import xyz.spoonmap.server.comment.repository.CommentRepository;
import xyz.spoonmap.server.comment.service.CommentService;
import xyz.spoonmap.server.exception.domain.comment.CommentNotFoundException;
import xyz.spoonmap.server.exception.domain.member.UnauthorizedException;
import xyz.spoonmap.server.exception.domain.post.PostNotFoundException;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.post.entity.Post;
import xyz.spoonmap.server.post.repository.PostRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceV1 implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Override
    public List<CommentResponseDto> findAllBy(Long postId) {
        return commentRepository.findCommentsByPostIdAndDeletedAtIsNull(postId)
                                .stream()
                                .map(CommentResponseDto::new)
                                .toList();
    }

    @Transactional
    @Override
    public CommentResponseDto create(UserDetails userDetails, Long postId, CommentSaveRequestDto requestDto) {
        Member member = ((CustomUserDetail) userDetails).getMember();
        Post post = postRepository.findPostByIdAndDeletedAtIsNull(postId).orElseThrow(PostNotFoundException::new);

        Comment parentComment = Optional.ofNullable(requestDto.parentCommentId())
                                        .map(id -> commentRepository.findById(id).orElseThrow(CommentNotFoundException::new))
                                        .orElse(null);

        Comment comment = Comment.builder()
                                 .post(post)
                                 .member(member)
                                 .parentComment(parentComment)
                                 .content(requestDto.content())
                                 .build();
        commentRepository.save(comment);
        return new CommentResponseDto(comment, post.getMember().getId());
    }

    @Transactional
    @Override
    public CommentResponseDto update(UserDetails userDetails, Long commentId, CommentUpdateRequestDto requestDto) {
        Member member = ((CustomUserDetail) userDetails).getMember();
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);

        if (!Objects.equals(comment.getMember().getId(), member.getId())) {
            throw new UnauthorizedException();
        }

        comment.update(requestDto);
        return new CommentResponseDto(comment);
    }

    @Override
    public Long delete(UserDetails userDetails, Long commentId) {
        Member member = ((CustomUserDetail) userDetails).getMember();
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);

        if (!Objects.equals(comment.getMember().getId(), member.getId())) {
            throw new UnauthorizedException();
        }

        comment.delete();
        return commentId;
    }
}
