package xyz.spoonmap.server.comment.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import xyz.spoonmap.server.category.entity.Category;
import xyz.spoonmap.server.category.repository.CategoryRepository;
import xyz.spoonmap.server.comment.entity.Comment;
import xyz.spoonmap.server.config.QueryDslConfig;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.member.repository.MemberRepository;
import xyz.spoonmap.server.post.entity.Post;
import xyz.spoonmap.server.post.entity.enums.MealTime;
import xyz.spoonmap.server.post.repository.PostRepository;
import xyz.spoonmap.server.restaurant.entity.Restaurant;
import xyz.spoonmap.server.restaurant.repository.RestaurantRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QueryDslConfig.class)
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PostRepository postRepository;

    private Member createMockMember() {
        return Member.builder()
                     .name("Minjun")
                     .email("test@gmail.com")
                     .password(".".repeat(60))
                     .nickname("MJ")
                     .build();
    }

    private Post createMockPost() {
        Member member = createMockMember();
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
        return Post.builder()
                   .member(member)
                   .restaurant(restaurant)
                   .category(category)
                   .title(expected)
                   .mealTime(MealTime.아침)
                   .starRating((byte) 10)
                   .build();
    }

    @Test
    void post_id로_삭제되지않은_댓글_조회() {
        Post post = createMockPost();
        postRepository.save(post);

        Member member = post.getMember();

        String expectedContent = "content1";
        Comment comment = Comment.builder()
                                 .content(expectedContent)
                                 .member(member)
                                 .post(post)
                                 .build();

        Comment deletedComment = Comment.builder()
                                        .content("content2")
                                        .member(member)
                                        .post(post)
                                        .build();

        commentRepository.save(comment);
        commentRepository.save(deletedComment);

        deletedComment.delete();

        List<Comment> result = commentRepository.findCommentsByPostIdAndDeletedAtIsNull(post.getId());

        Comment resultComment = result.get(0);

        assertThat(result).hasSize(1);
        assertThat(resultComment.getContent()).isEqualTo(expectedContent);

    }
}
