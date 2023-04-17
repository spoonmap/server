package xyz.spoonmap.server.spoon.service.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import xyz.spoonmap.server.authentication.CustomUserDetail;
import xyz.spoonmap.server.exception.domain.member.UnauthorizedException;
import xyz.spoonmap.server.exception.domain.post.PostNotFoundException;
import xyz.spoonmap.server.exception.domain.spoon.SpoonNotFoundException;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.post.entity.Post;
import xyz.spoonmap.server.post.repository.PostRepository;
import xyz.spoonmap.server.spoon.dto.SpoonResponseDto;
import xyz.spoonmap.server.spoon.entity.Spoon;
import xyz.spoonmap.server.spoon.repository.SpoonRepository;
import xyz.spoonmap.server.spoon.service.SpoonService;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class SpoonServiceV1 implements SpoonService {

    private final PostRepository postRepository;
    private final SpoonRepository spoonRepository;

    @Override
    public List<SpoonResponseDto> findAll(UserDetails userDetails, Long postId) {
        Member member = ((CustomUserDetail) userDetails).getMember();
        List<Spoon> spoons = spoonRepository.findAllByPostId(postId);

        return spoons.stream()
                     .map(spoon -> new SpoonResponseDto(spoon, member))
                     .toList();
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
    public Spoon.Pk delete(UserDetails userDetails, Long postId) {
        Member member = ((CustomUserDetail) userDetails).getMember();

        spoonRepository.deleteById(new Spoon.Pk(member.getId(), postId));
        return new Spoon.Pk(member.getId(), postId);
    }
}
