package xyz.spoonmap.server.spoon.service.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import xyz.spoonmap.server.authentication.CustomUserDetail;
import xyz.spoonmap.server.exception.domain.member.UnauthorizedException;
import xyz.spoonmap.server.exception.domain.post.PostNotFoundException;
import xyz.spoonmap.server.exception.domain.spoon.SpoonNotFoundException;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.post.entity.Post;
import xyz.spoonmap.server.post.repository.PostRepository;
import xyz.spoonmap.server.spoon.dto.SpoonDeleteResponseDto;
import xyz.spoonmap.server.spoon.dto.SpoonResponseDto;
import xyz.spoonmap.server.spoon.entity.Spoon;
import xyz.spoonmap.server.spoon.repository.SpoonRepository;
import xyz.spoonmap.server.spoon.service.SpoonService;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class SpoonServiceV1 implements SpoonService {

    private final PostRepository postRepository;
    private final SpoonRepository spoonRepository;

    @Override
    public Slice<SpoonResponseDto> findAll(UserDetails userDetails, Long postId, Pageable pageable) {
        Member member = ((CustomUserDetail) userDetails).getMember();
        Slice<Spoon> spoons = spoonRepository.findAllByPostId(postId, pageable);

        return spoons.map(spoon -> new SpoonResponseDto(spoon, member));
    }

    @Override
    public Long count(UserDetails userDetails, Long postId) {
        Member member = ((CustomUserDetail) userDetails).getMember();
        Long count = spoonRepository.countSpoonsByPostId(postId);
        return count;
    }

    @Override
    public SpoonResponseDto add(UserDetails userDetails, Long postId) {
        Member member = ((CustomUserDetail) userDetails).getMember();
        Post post = postRepository.findPostByIdAndDeletedAtIsNull(postId).orElseThrow(PostNotFoundException::new);

        Spoon spoon = Spoon.builder()
                           .member(member)
                           .post(post)
                           .build();
        spoonRepository.save(spoon);
        return new SpoonResponseDto(spoon, member);
    }

    @Override
    public SpoonDeleteResponseDto delete(UserDetails userDetails, Long postId) {
        Member member = ((CustomUserDetail) userDetails).getMember();

        Spoon spoon = spoonRepository.findById(new Spoon.Pk(member.getId(), postId))
                                     .orElseThrow(SpoonNotFoundException::new);
        if (!Objects.equals(spoon.getId().getMemberNo(), member.getId())) {
            throw new UnauthorizedException();
        }

        spoonRepository.delete(spoon);
        return new SpoonDeleteResponseDto(spoon.getId());
    }
}
