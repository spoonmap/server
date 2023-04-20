package xyz.spoonmap.server.spoon.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

import static java.lang.Math.random;
import static org.assertj.core.api.Assertions.assertThat;

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

    private Restaurant mockedRestaurant;
    private Category mockedCategory;
    private Post mockedPost;

    @BeforeEach
    public void setup() {
        mockedRestaurant = createMockRestaurant();
        mockedRestaurant = restaurantRepository.save(mockedRestaurant);
        mockedCategory = createMockCategory();
        mockedCategory = categoryRepository.save(mockedCategory);
        mockedPost = createMockPost();
        mockedPost = postRepository.save(mockedPost);
    }

    private Member createMockMember() {
        char[] array = new char[10]; // length is bounded by 7
        for (int i = 0; i < 10; i++) {
            array[i] = (char) ((int) (random() * 25) + 65);
        }
        String generatedname = new String(array);
        System.out.println(generatedname);

        return Member.builder()
                     .name(generatedname)
                     .email(generatedname + "@gmail.com")
                     .password(".".repeat(60))
                     .nickname(generatedname)
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

    private Post createMockPost() {
        Member member = createMockMember();
        memberRepository.save(member);
        return Post.builder()
                   .member(member)
                   .restaurant(mockedRestaurant)
                   .category(mockedCategory)
                   .title("mocked post")
                   .mealTime(MealTime.아침)
                   .starRating((byte) 10)
                   .build();
    }

    private void saveSpoon() {
        Member randomMember = createMockMember();
        memberRepository.save(randomMember);
        Spoon spoon = Spoon.builder()
                           .post(mockedPost)
                           .member(randomMember)
                           .build();
        spoon = spoonRepository.save(spoon);
    }

    @Test
    void 게시물_아이디로_스푼_전체_조회() {
        // given
        for (int i = 0; i < 30; i++) {
            saveSpoon();
        }

        // when
        Pageable pageable = PageRequest.of(1, 10);
        Slice<Spoon> results = spoonRepository.findAllByPostId(mockedPost.getId(), pageable);

        // then
        assertThat(results.getNumber()).isEqualTo(1);
        assertThat(results.getSize()).isEqualTo(10);
        assertThat(results.getContent()).hasSize(10);
        assertThat(results.getContent().get(0).getPost().getId()).isEqualTo(mockedPost.getId());
        assertThat(results.hasPrevious()).isTrue();
        assertThat(results.hasNext()).isTrue();
        assertThat(results.isFirst()).isFalse();
        assertThat(results.isLast()).isFalse();
    }

    @Test
    void 게시물_아이디로_스푼_갯수_조회() {
        // given
        Member member = createMockMember();
        memberRepository.save(member);

        Spoon spoon = Spoon.builder()
                           .post(mockedPost)
                           .member(member)
                           .build();
        spoon = spoonRepository.save(spoon);

        // when
        Long count = spoonRepository.countSpoonsByPostId(mockedPost.getId());

        // then
        assertThat(count).isEqualTo(1L);
    }
}
