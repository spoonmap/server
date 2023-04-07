package xyz.spoonmap.server.post.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import xyz.spoonmap.server.category.entity.Category;
import xyz.spoonmap.server.category.repository.CategoryRepository;
import xyz.spoonmap.server.config.QueryDslConfig;
import xyz.spoonmap.server.exception.post.PostNotFoundException;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.member.repository.MemberRepository;
import xyz.spoonmap.server.post.entity.Post;
import xyz.spoonmap.server.post.entity.enums.MealTime;
import xyz.spoonmap.server.restaurant.entity.Restaurant;
import xyz.spoonmap.server.restaurant.repository.RestaurantRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import(QueryDslConfig.class)
class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void 삭제되지_않은_게시물_모두_조회() {
        // given
        String password = "";
        for (int i = 0; i < 50; i++) {
            password += ".";
        }

        Member member = Member.builder()
                              .name("Minjun")
                              .email("test@gmail.com")
                              .password(password)
                              .nickname("MJ")
                              .build();
        memberRepository.save(member);

        Restaurant restaurant = Restaurant.builder()
                                          .name("restaurant")
                                          .address("restaurant address")
                                          .x(1.23F)
                                          .y(4.56F)
                                          .build();
        restaurantRepository.save(restaurant);

        Category category = new Category("한식");
        categoryRepository.save(category);

        String expected = "title";
        Post post = Post.builder()
                        .member(member)
                        .restaurant(restaurant)
                        .category(category)
                        .title(expected)
                        .mealTime(MealTime.아침)
                        .starRating((byte) 10)
                        .build();

        Post deleted = Post.builder()
                           .member(member)
                           .restaurant(restaurant)
                           .category(category)
                           .title("deleted title")
                           .mealTime(MealTime.아침)
                           .starRating((byte) 10)
                           .build();
        deleted.delete();

        postRepository.save(post);
        postRepository.save(deleted);

        // when
        List<Post> posts = postRepository.findByDeletedAtIsNull();
        Post resultPost = posts.get(0);

        assertThat(posts).hasSize(1);
        assertThat(resultPost.getTitle()).isEqualTo(expected);
    }

    @Test
    void 삭제되지_않은_게시물_하나_조회() {
        // given
        String password = "";
        for (int i = 0; i < 50; i++) {
            password += ".";
        }

        Member member = Member.builder()
                              .name("Minjun")
                              .email("test@gmail.com")
                              .password(password)
                              .nickname("MJ")
                              .build();
        memberRepository.save(member);

        Restaurant restaurant = Restaurant.builder()
                                          .name("restaurant")
                                          .address("restaurant address")
                                          .x(1.23F)
                                          .y(4.56F)
                                          .build();
        restaurantRepository.save(restaurant);

        Category category = new Category("한식");
        categoryRepository.save(category);

        String expected = "title";
        Post post = Post.builder()
                        .member(member)
                        .restaurant(restaurant)
                        .category(category)
                        .title(expected)
                        .mealTime(MealTime.아침)
                        .starRating((byte) 10)
                        .build();
        Post savedPost = postRepository.save(post);

        // when
        Post resultPost = postRepository.findPostByIdAndDeletedAtIsNull(savedPost.getId())
                                        .orElseThrow(PostNotFoundException::new);
        assertThat(resultPost.getTitle()).isEqualTo(expected);
    }

    @Test
    void 삭제되지_않은_게시물_하나_조회_실패() {
        // given
        String password = "";
        for (int i = 0; i < 50; i++) {
            password += ".";
        }

        Member member = Member.builder()
                              .name("Minjun")
                              .email("test@gmail.com")
                              .password(password)
                              .nickname("MJ")
                              .build();
        memberRepository.save(member);

        Restaurant restaurant = Restaurant.builder()
                                          .name("restaurant")
                                          .address("restaurant address")
                                          .x(1.23F)
                                          .y(4.56F)
                                          .build();
        restaurantRepository.save(restaurant);

        Category category = new Category("한식");
        categoryRepository.save(category);

        String expected = "title";
        Post post = Post.builder()
                        .member(member)
                        .restaurant(restaurant)
                        .category(category)
                        .title(expected)
                        .mealTime(MealTime.아침)
                        .starRating((byte) 10)
                        .build();
        post.delete();
        Post savedPost = postRepository.save(post);

        // when
        assertThatThrownBy(() -> {
            Post resultPost = postRepository.findPostByIdAndDeletedAtIsNull(savedPost.getId())
                                            .orElseThrow(PostNotFoundException::new);
        }).isInstanceOf(PostNotFoundException.class);
    }
}
