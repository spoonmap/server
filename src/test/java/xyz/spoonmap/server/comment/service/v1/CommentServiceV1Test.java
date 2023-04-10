package xyz.spoonmap.server.comment.service.v1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import xyz.spoonmap.server.authentication.CustomUserDetail;
import xyz.spoonmap.server.comment.dto.request.CommentSaveRequestDto;
import xyz.spoonmap.server.comment.dto.request.CommentUpdateRequestDto;
import xyz.spoonmap.server.comment.dto.response.CommentResponseDto;
import xyz.spoonmap.server.comment.entity.Comment;
import xyz.spoonmap.server.comment.repository.CommentRepository;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.post.entity.Post;
import xyz.spoonmap.server.post.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CommentServiceV1Test {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private PostRepository postRepository;
    @InjectMocks
    private CommentServiceV1 commentServiceV1;

    @Test
    void 모든_댓글_조회() {
        Long postId = 42L;
        String expected = "content";

        Comment comment = Comment.builder()
                                 .content(expected)
                                 .post(new Post())
                                 .member(new Member())
                                 .build();
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);
        given(commentRepository.findCommentsByPostIdAndDeletedAtIsNull(postId)).willReturn(comments);

        List<CommentResponseDto> responses = commentServiceV1.findAllBy(postId);

        CommentResponseDto response = responses.get(0);
        assertThat(response.content()).isEqualTo(expected);
        assertThat(response.parentCommentId()).isNull();
    }

    @Test
    @WithMockUser
    void 댓글_생성() {
        Long postId = 42L;

        Long memberId = 12L;
        Member member = Member.builder().build();
        ReflectionTestUtils.setField(member, "id", memberId);
        CustomUserDetail customUserDetail = new CustomUserDetail(member);

        Post post = Post.builder().build();
        given(postRepository.findPostByIdAndDeletedAtIsNull(postId)).willReturn(Optional.of(post));

        String expected = "content";
        CommentSaveRequestDto requestDto = new CommentSaveRequestDto(postId, null, expected);

        CommentResponseDto actual = commentServiceV1.create(customUserDetail, postId, requestDto);

        assertThat(actual.memberResponse().id()).isEqualTo(memberId);
        assertThat(actual.content()).isEqualTo(expected);
        assertThat(actual.parentCommentId()).isNull();
    }

    @Test
    void 댓글_수정() {
        Long memberId = 12L;
        Member member = Member.builder().build();
        ReflectionTestUtils.setField(member, "id", memberId);
        CustomUserDetail customUserDetail = new CustomUserDetail(member);

        Long commentId = 24L;
        Comment comment = Comment.builder()
                                 .member(member)
                                 .content("comment content")
                                 .build();
        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));


        String expected = "changed content";
        CommentUpdateRequestDto requestDto = new CommentUpdateRequestDto(expected);

        CommentResponseDto result = commentServiceV1.update(customUserDetail, commentId, requestDto);

        assertThat(result.content()).isEqualTo(expected);
    }

    @Test
    void 댓글_삭제() {
        Long memberId = 12L;
        Member member = Member.builder().build();
        ReflectionTestUtils.setField(member, "id", memberId);
        CustomUserDetail customUserDetail = new CustomUserDetail(member);

        Long commentId = 24L;
        Comment comment = Comment.builder()
                                 .member(member)
                                 .content("comment content")
                                 .build();
        ReflectionTestUtils.setField(comment, "id", commentId);
        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));

        Long result = commentServiceV1.delete(customUserDetail, commentId);

        assertThat(comment.getDeletedAt()).isNotNull();
        assertThat(result).isEqualTo(commentId);
    }
}
