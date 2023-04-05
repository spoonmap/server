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
import xyz.spoonmap.server.photo.repository.PhotoRepository;
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
    @Mock
    private PhotoRepository photoRepository;


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
    @DisplayName("모든 Post 조회")
    void getAllPosts() {
        List<Post> posts = List.of(createMockedPost());
        Long postId = 1L;
        ReflectionTestUtils.setField(posts.get(0), "id", postId);

        doReturn(posts).when(postRepository).findAll();
        doReturn(new ArrayList<>()).when(photoRepository).findByPostId(postId);

        List<PostResponseDto> postResponseList = postServiceV1.getAllPosts();

        assertThat(postResponseList).hasSize(1);
        assertThat(postResponseList.get(0).title()).isEqualTo(posts.get(0).getTitle());
    }

    @Test
    @DisplayName("단일 Post 조회 성공")
    void getPost() {
        Post post = createMockedPost();
        Long id = 42L;
        ReflectionTestUtils.setField(post, "id", id);

        doReturn(Optional.of(post)).when(postRepository).findById(id);
        doReturn(new ArrayList<>()).when(photoRepository).findByPostId(id);

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
    }

    @Test
    @DisplayName("Post 수정")
    void updatePost() {
    }
}