package xyz.spoonmap.server.post.service.v1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import xyz.spoonmap.server.category.entity.Category;
import xyz.spoonmap.server.category.repository.CategoryRepository;
import xyz.spoonmap.server.exception.post.PostNotFoundException;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.member.repository.MemberRepository;
import xyz.spoonmap.server.photo.entity.Photo;
import xyz.spoonmap.server.photo.dto.request.PhotoRequestDto;
import xyz.spoonmap.server.post.dto.request.PostRequestDto;
import xyz.spoonmap.server.restaurant.dto.request.RestaurantRequestDto;
import xyz.spoonmap.server.post.dto.response.PostResponseDto;
import xyz.spoonmap.server.post.entity.Post;
import xyz.spoonmap.server.post.entity.enums.MealTime;
import xyz.spoonmap.server.post.repository.PostRepository;
import xyz.spoonmap.server.restaurant.entity.Restaurant;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    private MemberRepository memberRepository;


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

        List<Photo> photos = new ArrayList<>();
        photos.add(new Photo("food_photo", "test.com/photo"));

        String title = "post title";
        String content = "post content";
        MealTime mealTime = MealTime.아침;
        Byte starRating = 4;
        return new Post(member, restaurant, category, photos, title, content, mealTime, starRating);
    }

    @Test
    @DisplayName("모든 Post 조회")
    void getAllPosts() {
        List<Post> posts = List.of(createMockedPost());

        doReturn(posts).when(postRepository).findAll();

        List<PostResponseDto> postResponseList = postServiceV1.getAllPosts();

        assertThat(postResponseList.size()).isEqualTo(1);
        assertThat(postResponseList.get(0).title()).isEqualTo(posts.get(0).getTitle());
    }

    @Test
    @DisplayName("단일 Post 조회 성공")
    void getPost() {
        Post post = createMockedPost();
        Long id = 42L;
        ReflectionTestUtils.setField(post, "id", id);

        doReturn(Optional.of(post)).when(postRepository).findById(id);

        PostResponseDto postResponseDto = postServiceV1.getPost(id);

        assertThat(postResponseDto.id()).isEqualTo(id);
    }

    @Test
    @DisplayName("단일 Post 조회 실패")
    void getPostFail() {
        Long id = 42L;

        doThrow(PostNotFoundException.class).when(postRepository).findById(id);

        assertThatThrownBy(() -> {
            postServiceV1.getPost(id);
        }).isInstanceOf(PostNotFoundException.class);
    }

    @Test
    @DisplayName("Post 생성")
    void createPost() {
        RestaurantRequestDto restaurantRequestDto = new RestaurantRequestDto("메가커피", "서울", 123.123F, 456.456F);
        List<PhotoRequestDto> photoRequestDtos = new ArrayList<>();
        photoRequestDtos.add(new PhotoRequestDto("메가커피 사진", "coffe.com/mega"));
        String title = "제목";
        String content = "내용";
        PostRequestDto postRequestDto = new PostRequestDto(restaurantRequestDto, photoRequestDtos, 1L, title, content, MealTime.아침, (byte) 4);

        Long memberId = 42L;
        Member member = new Member("test", "test@test.com", "test", "test", "test.com");
        doReturn(Optional.of(member)).when(memberRepository).findById(memberId);

        Long categoryId = 1L;
        Category category = new Category("한식");
        doReturn(Optional.of(category)).when(categoryRepository).findById(categoryId);

        PostResponseDto postResponseDto = postServiceV1.createPost(postRequestDto, memberId);

        assertThat(postResponseDto.title()).isEqualTo(title);
    }

    @Test
    @DisplayName("Post 수정")
    void updatePost() {
        String title = "title";
        String content = "content";
        Byte starRating = 10;
        String restaurantName = "restaurant";
        String restaurantAddress = "address";
        Float x = 123.123F;
        Float y = 456.456F;

        Member member = new Member("test", "test@test.com", "test", "test", "test.com");
        Restaurant restaurant = new Restaurant(restaurantName, restaurantAddress, x, y);
        Category category = new Category("한식");
        Post post = new Post(member, restaurant, category, null, title, content, MealTime.아침, starRating);

        String changedTitle = "changed title";
        String changedRestaurantName = "메가커피";
        String changedRestaurantAddress = "Seoul";

        RestaurantRequestDto restaurantRequestDto = new RestaurantRequestDto(changedRestaurantName, changedRestaurantAddress, x, y);
        Long categoryId = 1L;
        PostRequestDto postRequestDto = new PostRequestDto(restaurantRequestDto, null, categoryId, changedTitle, content, MealTime.저녁, starRating);

        Long postId = 42L;
        doReturn(Optional.of(post)).when(postRepository).findById(postId);
        doReturn(Optional.of(category)).when(categoryRepository).findById(categoryId);

        PostResponseDto postResponseDto = postServiceV1.updatePost(postId, postRequestDto);

        assertThat(postResponseDto.title()).isEqualTo(changedTitle);
        assertThat(postResponseDto.restaurant().getName()).isEqualTo(changedRestaurantName);
        assertThat(postResponseDto.restaurant().getAddress()).isEqualTo(changedRestaurantAddress);
    }
}
