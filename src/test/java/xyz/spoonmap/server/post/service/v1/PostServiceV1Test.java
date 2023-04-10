package xyz.spoonmap.server.post.service.v1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import xyz.spoonmap.server.authentication.CustomUserDetail;
import xyz.spoonmap.server.category.entity.Category;
import xyz.spoonmap.server.category.repository.CategoryRepository;
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
import xyz.spoonmap.server.post.entity.enums.MealTime;
import xyz.spoonmap.server.post.repository.PostRepository;
import xyz.spoonmap.server.restaurant.dto.request.RestaurantRequestDto;
import xyz.spoonmap.server.restaurant.entity.Restaurant;
import xyz.spoonmap.server.restaurant.repository.RestaurantRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PostServiceV1Test {

    @InjectMocks
    private PostServiceV1 postServiceV1;
    @Mock
    private PostRepository postRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PhotoRepository photoRepository;
    @Mock
    private S3Adapter s3Adapter;

    @Test
    void 모든_post_조회() {
        Long postId = 42L;
        Post post = Post.builder()
                        .member(new Member())
                        .restaurant(new Restaurant())
                        .category(new Category())
                        .build();
        ReflectionTestUtils.setField(post, "id", postId);

        List<Post> posts = new ArrayList<>();
        posts.add(post);

        given(postRepository.findByDeletedAtIsNull()).willReturn(posts);

        String expected = "test.com";
        Photo photo = Photo.builder()
                           .url(expected)
                           .build();
        List<Photo> photos = new ArrayList<>();
        photos.add(photo);

        given(photoRepository.findByPostIdAndDeletedAtIsNull(postId)).willReturn(photos);

        List<PostResponseDto> postResponseList = postServiceV1.getAllPosts();

        PostResponseDto result = postResponseList.get(0);
        assertThat(postResponseList).hasSize(1);
        assertThat(result.photoUrls()).hasSize(1);
        assertThat(result.photoUrls().get(0)).isEqualTo(expected);
    }

    @Test
    void post_한개_조회_성공() {
        Long postId = 42L;
        Post post = Post.builder()
                        .member(new Member())
                        .restaurant(new Restaurant())
                        .category(new Category())
                        .build();
        ReflectionTestUtils.setField(post, "id", postId);

        given(postRepository.findPostByIdAndDeletedAtIsNull(postId)).willReturn(Optional.of(post));

        String expected = "test.com";
        Photo photo = Photo.builder()
                           .url(expected)
                           .build();
        List<Photo> photos = new ArrayList<>();
        photos.add(photo);

        given(photoRepository.findByPostIdAndDeletedAtIsNull(postId)).willReturn(photos);

        PostResponseDto result = postServiceV1.getPost(postId);

        assertThat(result.id()).isEqualTo(postId);
        assertThat(result.photoUrls().get(0)).isEqualTo(expected);
    }

    @Test
    void post_한개_조회_실패() {
        Long postId = 42L;

        given(postRepository.findPostByIdAndDeletedAtIsNull(postId)).willThrow(PostNotFoundException.class);

        assertThatThrownBy(() -> {
            postServiceV1.getPost(postId);
        }).isInstanceOf(PostNotFoundException.class);
    }

    @Test
    @DisplayName("Post 생성")
    void createPost() {
        Long memberId = 42L;
        Member member = Member.builder().build();
        ReflectionTestUtils.setField(member, "id", memberId);
        CustomUserDetail customUserDetail = new CustomUserDetail(member);

        String title = "post title";
        String content = "post content";
        RestaurantRequestDto restaurantRequestDto = new RestaurantRequestDto("restaurant name", "restaurant address", 1.2F, 3.4F);
        PostSaveRequestDto postRequestDto = new PostSaveRequestDto(restaurantRequestDto, 1L, title, content, MealTime.아침, (byte) 10);

        List<MultipartFile> files = new ArrayList<>();

        Optional<Category> category = Optional.of(new Category("한식"));
        given(categoryRepository.findById(postRequestDto.categoryId())).willReturn(category);

        PostResponseDto result = postServiceV1.createPost(customUserDetail, postRequestDto, files);

        assertThat(result.author().id()).isEqualTo(memberId);
        assertThat(result.title()).isEqualTo(title);
        assertThat(result.content()).isEqualTo(content);
    }

    @Test
    void 작성자가_아닌_사람이_Post_수정() {
        Long memberId = 42L;
        Member member = Member.builder().build();
        ReflectionTestUtils.setField(member, "id", memberId);
        CustomUserDetail customUserDetail = new CustomUserDetail(member);

        Long authorId = 24L;
        Member author = Member.builder().build();
        ReflectionTestUtils.setField(member, "id", authorId);

        Long postId = 12L;
        Optional<Post> post = Optional.of(Post.builder()
                                              .member(author)
                                              .build());
        given(postRepository.findPostByIdAndDeletedAtIsNull(postId)).willReturn(post);

        assertThatThrownBy(() -> {
            postServiceV1.updatePost(customUserDetail, postId, null, null);
        }).isInstanceOf(PostNotFoundException.class);
    }

    @Test
    void post_수정() {
        Long memberId = 42L;
        Member member = Member.builder().build();
        ReflectionTestUtils.setField(member, "id", memberId);
        CustomUserDetail customUserDetail = new CustomUserDetail(member);

        Long postId = 4L;
        String title = "updated title";
        String content = "updated content";

        RestaurantRequestDto restaurantRequestDto = new RestaurantRequestDto("restaurant name", "restaurant address", 1.2F, 3.4F);
        PostUpdateRequestDto postRequestDto = new PostUpdateRequestDto(restaurantRequestDto, 1L, title, content, MealTime.아침, (byte) 10);

        Post post = Post.builder()
                        .member(member)
                        .build();
        ReflectionTestUtils.setField(post, "id", postId);

        given(postRepository.findPostByIdAndDeletedAtIsNull(postId)).willReturn(Optional.of(post));

        Category category = new Category("한식");
        given(categoryRepository.findById(postRequestDto.categoryId())).willReturn(Optional.of(category));

        given(photoRepository.findByPostId(postId)).willReturn(new ArrayList<Photo>());

        PostResponseDto result = postServiceV1.updatePost(customUserDetail, postId, postRequestDto, null);

        assertThat(result.title()).isEqualTo(title);
        assertThat(result.content()).isEqualTo(content);
    }
}
