package xyz.spoonmap.server.post.service.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.spoonmap.server.category.entity.Category;
import xyz.spoonmap.server.category.repository.CategoryRepository;
import xyz.spoonmap.server.exception.category.CategoryNotFoundException;
import xyz.spoonmap.server.exception.member.MemberNotFoundException;
import xyz.spoonmap.server.exception.photo.PhotoUploadException;
import xyz.spoonmap.server.exception.post.PostNotFoundException;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.member.repository.MemberRepository;
import xyz.spoonmap.server.photo.adapter.S3Adapter;
import xyz.spoonmap.server.photo.entity.Photo;
import xyz.spoonmap.server.photo.repository.PhotoRepository;
import xyz.spoonmap.server.post.dto.request.PostRequestDto;
import xyz.spoonmap.server.post.dto.response.PostResponseDto;
import xyz.spoonmap.server.post.entity.Post;
import xyz.spoonmap.server.post.repository.PostRepository;
import xyz.spoonmap.server.post.service.PostService;
import xyz.spoonmap.server.restaurant.entity.Restaurant;
import xyz.spoonmap.server.restaurant.repository.RestaurantRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceV1 implements PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;
    private final PhotoRepository photoRepository;
    private final S3Adapter s3Adapter;

    @Override
    public List<PostResponseDto> getAllPosts() {
        List<Post> posts = this.postRepository.findAll();

        posts = posts.stream().filter(post -> Objects.isNull(post.getDeletedAt())).toList();

        return posts.stream().map(post -> {
            String[] urls = photoRepository.findByPostId(post.getId())
                    .stream()
                    .filter(photo -> Objects.isNull(photo.getDeletedAt()))
                    .map(Photo::getUrl)
                    .toArray(String[]::new);
            return new PostResponseDto(post, urls);
        }).toList();
    }

    @Override
    public PostResponseDto getPost(Long id) {
        Post post = this.postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        if (Objects.nonNull(post.getDeletedAt())) {
            throw new PostNotFoundException();
        }
        String[] urls = photoRepository.findByPostId(post.getId())
                .stream()
                .filter(photo -> Objects.isNull(photo.getDeletedAt()))
                .map(Photo::getUrl)
                .toArray(String[]::new);
        return new PostResponseDto(post, urls);
    }

    @Override
    @Transactional
    public PostResponseDto createPost(Long memberId, PostRequestDto postRequestDto, List<MultipartFile> files) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);

        Restaurant restaurant = Restaurant.builder()
                .name(postRequestDto.restaurant().name())
                .address(postRequestDto.restaurant().address())
                .x(postRequestDto.restaurant().x())
                .y(postRequestDto.restaurant().y()).build();
        restaurantRepository.save(restaurant);

        Category category = categoryRepository.findById(postRequestDto.categoryId())
                .orElseThrow(CategoryNotFoundException::new);

        Post post = Post.builder()
                .member(member)
                .restaurant(restaurant)
                .category(category)
                .title(postRequestDto.title())
                .content(postRequestDto.content())
                .mealTime(postRequestDto.mealTime())
                .starRating(postRequestDto.starRating()).build();
        this.postRepository.save(post);

        List<Photo> photos = savePhotosFromFile(post, files);
        String[] urls = photos.stream().map(Photo::getUrl).toArray(String[]::new);

        return new PostResponseDto(post, urls);
    }

    @Override
    @Transactional
    public PostResponseDto updatePost(Long memberId, Long id, PostRequestDto postRequestDto, List<MultipartFile> files) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);

        if (!Objects.equals(post.getMember().getId(), memberId)) {
            throw new PostNotFoundException(); // TODO: UnAuthorized (401)로 수정
        }

        Category category = categoryRepository.findById(postRequestDto.categoryId()).orElseThrow(CategoryNotFoundException::new);

        Restaurant restaurant = Restaurant.builder()
                .name(postRequestDto.restaurant().name())
                .address(postRequestDto.restaurant().address())
                .x(postRequestDto.restaurant().x())
                .y(postRequestDto.restaurant().y()).build();
        restaurantRepository.save(restaurant);

        post.update(restaurant, category, postRequestDto.title(), postRequestDto.content(), postRequestDto.mealTime(), postRequestDto.starRating());

        List<Photo> photos = photoRepository.findByPostId(post.getId());
        photoRepository.deleteAll(photos);

        List<Photo> newPhotos = savePhotosFromFile(post, files);
        String[] urls = newPhotos.stream().map(Photo::getUrl).toArray(String[]::new);

        return new PostResponseDto(post, urls);
    }

    private List<Photo> savePhotosFromFile(Post post, List<MultipartFile> files) {
        List<Photo> photos = Optional.ofNullable(files)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(file -> {
                    try {
                        return Photo.builder()
                                .originName(file.getOriginalFilename())
                                .url(s3Adapter.upload(file))
                                .post(post)
                                .build();
                    } catch (IOException e) {
                        throw new PhotoUploadException();
                    }
                })
                .toList();
        photoRepository.saveAll(photos);
        return photos;
    }

    @Override
    @Transactional
    public PostResponseDto deletePost(Long memberId, Long id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        if (!Objects.equals(post.getMember().getId(), memberId)) {
            throw new PostNotFoundException(); // TODO: UnAuthorized
        }

        post.delete();

        List<Photo> photos = photoRepository.findByPostId(id);
        photos.forEach(Photo::delete);

        String[] urls = photoRepository.findByPostId(post.getId())
                .stream()
                .filter(photo -> Objects.isNull(photo.getDeletedAt()))
                .map(Photo::getUrl)
                .toArray(String[]::new);

        return new PostResponseDto(post, urls);
    }
}
