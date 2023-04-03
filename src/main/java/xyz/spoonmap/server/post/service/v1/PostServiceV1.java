package xyz.spoonmap.server.post.service.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.spoonmap.server.category.entity.Category;
import xyz.spoonmap.server.category.repository.CategoryRepository;
import xyz.spoonmap.server.exception.category.CategoryNotFoundException;
import xyz.spoonmap.server.exception.member.MemberNotFoundException;
import xyz.spoonmap.server.exception.post.PostNotFoundException;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.member.repository.MemberRepository;
import xyz.spoonmap.server.photo.entity.Photo;
import xyz.spoonmap.server.post.dto.request.PostRequestDto;
import xyz.spoonmap.server.post.dto.response.PostResponseDto;
import xyz.spoonmap.server.post.entity.Post;
import xyz.spoonmap.server.post.repository.PostRepository;
import xyz.spoonmap.server.post.service.PostService;
import xyz.spoonmap.server.restaurant.entity.Restaurant;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceV1 implements PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;

    @Override
    public List<PostResponseDto> getAllPosts() {
        List<Post> posts = this.postRepository.findAll();
        return posts.stream().map(PostResponseDto::new).collect(Collectors.toList());
    }

    @Override
    public PostResponseDto getPost(Long id) {
        Post post = this.postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        return new PostResponseDto(post);
    }

    @Override
    @Transactional
    public PostResponseDto createPost(PostRequestDto postRequestDto, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        Restaurant restaurant = Restaurant.builder()
                .name(postRequestDto.restaurant().name())
                .address(postRequestDto.restaurant().address())
                .x(postRequestDto.restaurant().x())
                .y(postRequestDto.restaurant().y()).build();
        Category category = categoryRepository.findById(postRequestDto.categoryId())
                .orElseThrow(CategoryNotFoundException::new);
        List<Photo> photos = postRequestDto.photos()
                .stream()
                .map((photoDto -> Photo.builder()
                        .originName(photoDto.originName())
                        .url(photoDto.url())
                        .build()))
                .toList();
        Post post = Post.builder()
                .member(member)
                .restaurant(restaurant)
                .category(category)
                .photos(photos)
                .title(postRequestDto.title())
                .content(postRequestDto.content())
                .mealTime(postRequestDto.mealTime())
                .starRating(postRequestDto.starRating()).build();
        this.postRepository.save(post);
        return new PostResponseDto(post);
    }

    @Override
    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto postRequestDto) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);

        Category category = categoryRepository.findById(postRequestDto.categoryId()).orElseThrow(CategoryNotFoundException::new);

        Restaurant restaurant = Restaurant.builder()
                .name(postRequestDto.restaurant().name())
                .address(postRequestDto.restaurant().address())
                .x(postRequestDto.restaurant().x())
                .y(postRequestDto.restaurant().y()).build();

        List<Photo> photos = Optional.ofNullable(postRequestDto.photos())
                .orElseGet(Collections::emptyList)
                .stream()
                .map((photoDto -> Photo.builder()
                        .originName(photoDto.originName())
                        .url(photoDto.url())
                        .build()))
                .toList();

        post.update(restaurant, category, photos, postRequestDto.title(), postRequestDto.content(), postRequestDto.mealTime(), postRequestDto.starRating());
        return new PostResponseDto(post);
    }

    @Override
    @Transactional
    public void deletePost(Long id) {
        this.postRepository.deleteById(id);
    }
}
