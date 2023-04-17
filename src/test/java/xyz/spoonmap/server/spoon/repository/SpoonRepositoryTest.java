package xyz.spoonmap.server.spoon.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import xyz.spoonmap.server.category.entity.Category;
import xyz.spoonmap.server.category.repository.CategoryRepository;
import xyz.spoonmap.server.config.QueryDslConfig;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.member.repository.MemberRepository;
import xyz.spoonmap.server.post.entity.Post;
import xyz.spoonmap.server.post.entity.enums.MealTime;
import xyz.spoonmap.server.post.repository.PostRepository;
import xyz.spoonmap.server.restaurant.entity.Restaurant;
import xyz.spoonmap.server.restaurant.repository.RestaurantRepository;
import xyz.spoonmap.server.spoon.entity.Spoon;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@DataJpaTest
@Import(QueryDslConfig.class)
class SpoonRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SpoonRepository spoonRepository;

    private Member createMockMember() {
        return Member.builder()
                     .name("test")
                     .email("test@gmail.com")
                     .password(".".repeat(60))
                     .nickname("TU")
                     .build();
    }

    private Restaurant createMockRestaurant() {
        return Restaurant.builder()
                         .name("mocked restaurant")
                         .address("mocked address")
                         .x(1.23F)
                         .y(4.56F)
                         .build();
    }

    private Category createMockCategory() {
        return new Category("한식");
    }

    private Post createMockPost(Member member, Restaurant restaurant, Category category) {
        return Post.builder()
                   .member(member)
                   .restaurant(restaurant)
                   .category(category)
                   .title("mocked post")
                   .mealTime(MealTime.아침)
                   .starRating((byte) 10)
                   .build();
    }

    @Test
    void 게시물_아이디로_spoon_전체_조회() {
        // given
        Member mockedMember = createMockMember();
        mockedMember = memberRepository.save(mockedMember);

        Restaurant mockedRestaurant = createMockRestaurant();
        mockedRestaurant = restaurantRepository.save(mockedRestaurant);

        Category mockedCategory = createMockCategory();
        mockedCategory = categoryRepository.save(mockedCategory);

        Post mockedPost = createMockPost(mockedMember, mockedRestaurant, mockedCategory);
        mockedPost = postRepository.save(mockedPost);

        Spoon spoon = Spoon.builder()
                           .post(mockedPost)
                           .member(mockedMember)
                           .build();
        spoon = spoonRepository.save(spoon);

        // when
        List<Spoon> results = spoonRepository.findAllByPostId(mockedPost.getId());

        // then
        Spoon result = results.get(0);
        assertThat(results).hasSize(1);
        assertThat(result.getId().getPostNo()).isEqualTo(mockedPost.getId());
        assertThat(result.getId().getMemberNo()).isEqualTo(mockedMember.getId());
    }
}
