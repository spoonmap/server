package xyz.spoonmap.server.post.service.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.spoonmap.server.category.entity.Category;
import xyz.spoonmap.server.category.repository.CategoryRepository;
import xyz.spoonmap.server.exception.domain.category.CategoryNotFoundException;
import xyz.spoonmap.server.exception.domain.member.MemberNotFoundException;
import xyz.spoonmap.server.exception.domain.photo.PhotoUploadException;
import xyz.spoonmap.server.exception.domain.post.PostNotFoundException;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.member.repository.MemberRepository;
import xyz.spoonmap.server.photo.adapter.S3Adapter;
import xyz.spoonmap.server.photo.entity.Photo;
import xyz.spoonmap.server.photo.repository.PhotoRepository;
import xyz.spoonmap.server.post.dto.request.PostSaveRequestDto;
import xyz.spoonmap.server.post.dto.request.PostUpdateRequestDto;
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
        List<Post> posts = this.postRepository.findByDeletedAtIsNull();

        return posts.stream().map(post -> {
            List<String> urls = photoRepository.findByPostIdAndDeletedAtIsNull(post.getId())
                                               .stream()
                                               .map(Photo::getUrl)
                                               .toList();
            return new PostResponseDto(post, urls);
        }).toList();
    }

    @Override
    public PostResponseDto getPost(Long id) {
        Post post = this.postRepository.findPostByIdAndDeletedAtIsNull(id)
                                       .orElseThrow(PostNotFoundException::new);

        List<String> urls = photoRepository.findByPostIdAndDeletedAtIsNull(post.getId())
                                           .stream()
                                           .map(Photo::getUrl)
                                           .toList();
        return new PostResponseDto(post, urls);
    }

    @Override
    @Transactional
    public PostResponseDto createPost(Long memberId, PostSaveRequestDto requestDto, List<MultipartFile> files) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);

        Restaurant restaurant = requestDto.restaurant().toEntity();
        restaurantRepository.save(restaurant);

        Category category = categoryRepository.findById(requestDto.categoryId())
                                              .orElseThrow(CategoryNotFoundException::new);

        Post post = requestDto.toEntity(member, restaurant, category);
        this.postRepository.save(post);

        List<String> urls = savePhotos(post, files).stream()
                                                   .map(Photo::getUrl)
                                                   .toList();
        return new PostResponseDto(post, urls);
    }

    @Override
    @Transactional
    public PostResponseDto updatePost(Long memberId, Long id, PostUpdateRequestDto requestDto, List<MultipartFile> files) {
        Post post = postRepository.findPostByIdAndDeletedAtIsNull(id)
                                  .orElseThrow(PostNotFoundException::new);

        if (!Objects.equals(post.getMember().getId(), memberId)) {
            throw new PostNotFoundException(); // TODO: UnAuthorized (401)로 수정
        }

        Category category = categoryRepository.findById(requestDto.categoryId())
                                              .orElseThrow(CategoryNotFoundException::new);

        Restaurant restaurant = requestDto.restaurant().toEntity();
        restaurantRepository.save(restaurant);

        post.update(requestDto, restaurant, category);

        List<Photo> photos = photoRepository.findByPostId(post.getId());
        photos.forEach(Photo::delete);

        List<String> urls = savePhotos(post, files).stream()
                                                   .map(Photo::getUrl)
                                                   .toList();
        return new PostResponseDto(post, urls);
    }

    private List<Photo> savePhotos(Post post, List<MultipartFile> files) {
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
        Post post = postRepository.findPostByIdAndDeletedAtIsNull(id)
                                  .orElseThrow(PostNotFoundException::new);

        if (!Objects.equals(post.getMember().getId(), memberId)) {
            throw new PostNotFoundException(); // TODO: UnAuthorized
        }
        post.delete();

        List<Photo> photos = photoRepository.findByPostId(id);
        photos.forEach(Photo::delete);

        List<String> urls = photoRepository.findByPostIdAndDeletedAtIsNull(post.getId())
                                           .stream()
                                           .map(Photo::getUrl)
                                           .toList();
        return new PostResponseDto(post, urls);
    }
}
