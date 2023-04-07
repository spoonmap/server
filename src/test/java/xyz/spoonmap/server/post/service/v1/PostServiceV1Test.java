package xyz.spoonmap.server.post.service.v1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

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


    private Post createMockedPost() {
        String name = "test";
        String email = "test@test.com";
        String password = "testPass";
        String nickname = "test";
        String avatar = "test.com";
        Member member = new Member(name, email, password, nickname, avatar);

        String restaurantName = "test restaurant";
        String restaurantAddress = "south korea";
        Float x = 123.123F;
        Float y = 321.321F;
        Restaurant restaurant = new Restaurant(restaurantName, restaurantAddress, x, y);

        Category category = new Category("한식");

        String title = "post title";
        String content = "post content";
        MealTime mealTime = MealTime.아침;
        Byte starRating = 4;
        return new Post(member, restaurant, category, title, content, mealTime, starRating);

    }

    @Test
    void 모든_post_조회() {
        List<Post> posts = List.of(createMockedPost());
        Long postId = 1L;
        ReflectionTestUtils.setField(posts.get(0), "id", postId);

        doReturn(posts).when(postRepository).findByDeletedAtIsNull();
        doReturn(new ArrayList<>()).when(photoRepository).findByPostIdAndDeletedAtIsNull(postId);

        List<PostResponseDto> postResponseList = postServiceV1.getAllPosts();

        Post expected = posts.get(0);
        PostResponseDto actual = postResponseList.get(0);

        assertThat(postResponseList).hasSize(1);
        assertThat(actual.title()).isEqualTo(expected.getTitle());
    }

    @Test
    void post_한개_조회_성공() {
        Post post = createMockedPost();
        Long id = 42L;
        ReflectionTestUtils.setField(post, "id", id);

        doReturn(Optional.of(post)).when(postRepository).findPostByIdAndDeletedAtIsNull(id);
        doReturn(new ArrayList<>()).when(photoRepository).findByPostIdAndDeletedAtIsNull(id);

        PostResponseDto postResponseDto = postServiceV1.getPost(id);

        assertThat(postResponseDto.id()).isEqualTo(id);
    }

    @Test
    void post_한개_조회_실패() {
        Long id = 42L;

        doThrow(PostNotFoundException.class).when(postRepository).findPostByIdAndDeletedAtIsNull(id);

        assertThatThrownBy(() -> {
            postServiceV1.getPost(id);
        }).isInstanceOf(PostNotFoundException.class);
    }

    @Test
    @DisplayName("Post 생성")
    void createPost() {
        Long memberId = 42L;
        String title = "post title";
        String content = "post content";

        RestaurantRequestDto restaurantRequestDto = new RestaurantRequestDto("restaurant name", "restaurant address", 1.2F, 3.4F);
        PostSaveRequestDto postRequestDto = new PostSaveRequestDto(restaurantRequestDto, 1L, title, content, MealTime.아침, (byte) 10);
        List<MultipartFile> files = new ArrayList<>();

        Optional<Member> member = Optional.of(new Member());
        given(memberRepository.findById(memberId)).willReturn(member);

        Optional<Category> category = Optional.of(new Category("한식"));
        given(categoryRepository.findById(postRequestDto.categoryId())).willReturn(category);

        PostResponseDto postResponseDto = postServiceV1.createPost(memberId, postRequestDto, files);

        assertThat(postResponseDto.title()).isEqualTo(title);
        assertThat(postResponseDto.content()).isEqualTo(content);
    }

    @Test
    void 작성자가_아닌_사람이_Post_수정() {
        Long memberId = 42L;
        Long postId = 1L;

        String title = "post title";
        String content = "post content";
        RestaurantRequestDto restaurantRequestDto = new RestaurantRequestDto("restaurant name", "restaurant address", 1.2F, 3.4F);
        PostUpdateRequestDto requestDto = new PostUpdateRequestDto(restaurantRequestDto, 1L, title, content, MealTime.아침, (byte) 10);
        List<MultipartFile> files = new ArrayList<>();


        Member member = new Member();
        Long actualMemberId = 1L;
        ReflectionTestUtils.setField(member, "id", actualMemberId);

        Optional<Post> post = Optional.of(Post.builder()
                                              .member(member)
                                              .build());
        given(postRepository.findPostByIdAndDeletedAtIsNull(postId)).willReturn(post);

        assertThatThrownBy(() -> {
            postServiceV1.updatePost(memberId, postId, requestDto, files);
        }).isInstanceOf(PostNotFoundException.class);
    }

    @Test
    void post_수정() {
        Long memberId = 42L;
        Long postId = 4L;

        String title = "updated title";
        String content = "updated content";

        RestaurantRequestDto restaurantRequestDto = new RestaurantRequestDto("restaurant name", "restaurant address", 1.2F, 3.4F);
        PostUpdateRequestDto postRequestDto = new PostUpdateRequestDto(restaurantRequestDto, 1L, title, content, MealTime.아침, (byte) 10);
        List<MultipartFile> files = new ArrayList<>();

        Member member = new Member();
        ReflectionTestUtils.setField(member, "id", memberId);

        Post post = Post.builder()
                        .member(member)
                        .build();
        ReflectionTestUtils.setField(post, "id", postId);

        given(postRepository.findPostByIdAndDeletedAtIsNull(postId)).willReturn(Optional.of(post));

        Category category = new Category("한식");
        given(categoryRepository.findById(postRequestDto.categoryId())).willReturn(Optional.of(category));

        given(photoRepository.findByPostId(postId)).willReturn(new ArrayList<Photo>());

        PostResponseDto responseDto = postServiceV1.updatePost(memberId, postId, postRequestDto, files);

        assertThat(responseDto.title()).isEqualTo(title);
        assertThat(responseDto.content()).isEqualTo(content);
    }
}
