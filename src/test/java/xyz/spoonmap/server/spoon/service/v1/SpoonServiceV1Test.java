package xyz.spoonmap.server.spoon.service.v1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;
import xyz.spoonmap.server.authentication.CustomUserDetail;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.post.entity.Post;
import xyz.spoonmap.server.post.repository.PostRepository;
import xyz.spoonmap.server.spoon.dto.SpoonResponseDto;
import xyz.spoonmap.server.spoon.entity.Spoon;
import xyz.spoonmap.server.spoon.repository.SpoonRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class SpoonServiceV1Test {

    private final Long postId = 42L;
    private final Long memberId = 12L;
    @Mock
    private PostRepository postRepository;
    @Mock
    private SpoonRepository spoonRepository;
    @InjectMocks
    private SpoonServiceV1 spoonServiceV1;
    private UserDetails userDetails = new CustomUserDetail(createMember());

    private Member createMember() {
        Member member = new Member();
        ReflectionTestUtils.setField(member, "id", memberId);
        return member;
    }

    @Test
    void 게시글의_모든_스푼_조회() {
        // given
        Post post = new Post();
        ReflectionTestUtils.setField(post, "id", postId);

        Spoon spoon = Spoon.builder()
                           .member(((CustomUserDetail) userDetails).getMember())
                           .post(post)
                           .build();
        ReflectionTestUtils.setField(spoon, "id", new Spoon.Pk(memberId, postId));
        given(spoonRepository.findAllByPostId(postId)).willReturn(List.of(spoon));

        // when
        List<SpoonResponseDto> results = spoonServiceV1.findAll(userDetails, postId);

        // then
        SpoonResponseDto result = results.get(0);
        assertThat(result.postId()).isEqualTo(postId);
        assertThat(result.memberResponse().id()).isEqualTo(memberId);
    }

    @Test
    void 게시물의_스푼_갯수_조회() {
        Long spoonCount = 24L;
        given(spoonRepository.countSpoonsByPostId(postId)).willReturn(spoonCount);

        Long result = spoonServiceV1.count(userDetails, postId);

        assertThat(result).isEqualTo(spoonCount);
    }

    @Test
    void 게시물_스푼_추가() {
        // given
        Post post = new Post();
        ReflectionTestUtils.setField(post, "id", postId);
        given(postRepository.findPostByIdAndDeletedAtIsNull(postId)).willReturn(Optional.of(post));

        // when
        SpoonResponseDto result = spoonServiceV1.add(userDetails, postId);

        // then
        assertThat(result.postId()).isEqualTo(postId);
        assertThat(result.memberResponse().id()).isEqualTo(memberId);
    }

    @Test
    void 게시물_스푼_삭제() {
        // given

        // when
        Spoon.Pk id = spoonServiceV1.delete(userDetails, postId);

        // then
        assertThat(id.getPostNo()).isEqualTo(postId);
        assertThat(id.getMemberNo()).isEqualTo(memberId);
    }
}
