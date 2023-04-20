package xyz.spoonmap.server.notification.aop;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;
import xyz.spoonmap.server.authentication.CustomUserDetail;
import xyz.spoonmap.server.category.entity.Category;
import xyz.spoonmap.server.category.repository.CategoryRepository;
import xyz.spoonmap.server.comment.dto.request.CommentSaveRequestDto;
import xyz.spoonmap.server.comment.dto.response.CommentResponseDto;
import xyz.spoonmap.server.comment.repository.CommentRepository;
import xyz.spoonmap.server.comment.service.CommentService;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.member.repository.MemberRepository;
import xyz.spoonmap.server.notification.entity.Notification;
import xyz.spoonmap.server.notification.entity.enums.NotificationType;
import xyz.spoonmap.server.notification.event.NotificationEvent;
import xyz.spoonmap.server.notification.event.eventlistener.NotificationEventListener;
import xyz.spoonmap.server.notification.repository.NotificationRepository;
import xyz.spoonmap.server.post.entity.Post;
import xyz.spoonmap.server.post.entity.enums.MealTime;
import xyz.spoonmap.server.post.repository.PostRepository;
import xyz.spoonmap.server.relation.dto.response.FollowAddResponse;
import xyz.spoonmap.server.relation.repository.RelationRepository;
import xyz.spoonmap.server.relation.service.RelationService;
import xyz.spoonmap.server.restaurant.entity.Restaurant;
import xyz.spoonmap.server.restaurant.repository.RestaurantRepository;

@SpringBootTest
@RecordApplicationEvents
class NotificationAspectTest {

    @Autowired
    ApplicationEventPublisher publisher;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    RelationRepository relationRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CommentService commentService;

    @Autowired
    RelationService relationService;

    @Autowired
    NotificationEventListener listener;

    @Autowired
    EntityManager em;

    @SpyBean
    NotificationEventListener notificationEventListener;

    @SpyBean
    NotificationAspect notificationAspect;

    UserDetails userDetails;
    Member member1;
    Member member2;
    String password = "passw0rd";
    String encoded;

    @BeforeEach
    void setUp() {
        encoded = passwordEncoder.encode(password);

        member1 = new Member("김철수", "asdffsd@email.com", encoded, "철수99", null);
        memberRepository.save(member1);

        member2 = new Member("김영희", "xzcv@email.com", encoded, "영희99", null);
        memberRepository.save(member2);

        userDetails = new CustomUserDetail(member2);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, ""));
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
        notificationRepository.deleteAll();
        relationRepository.deleteAll();
        commentRepository.deleteAll();
        postRepository.deleteAll();
        memberRepository.deleteAll();
        em.flush();
        em.clear();
    }

    @Transactional
    @Test
    @DisplayName("댓글 추가시 알림 생성")
    void testAddCommentNotification() throws Exception {
        Restaurant restaurant = new Restaurant("식당1", "서울", 1.23f, 4.56f);
        Category category = new Category("분류1");
        Post post = Post.builder()
                        .member(member1)
                        .restaurant(restaurant)
                        .category(category)
                        .title("title")
                        .content("content")
                        .mealTime(MealTime.점심)
                        .starRating((byte) 3)
                        .build();

        restaurantRepository.save(restaurant);
        categoryRepository.save(category);
        memberRepository.save(member1);
        memberRepository.save(member2);
        postRepository.save(post);

        CommentSaveRequestDto dto = new CommentSaveRequestDto(post.getId(), null, "content");

        int threads = 1;
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        executor.submit(() -> commentService.create(userDetails, post.getId(), dto));
        if (executor.awaitTermination(1, TimeUnit.SECONDS)) {
            executor.shutdown();

            List<Notification> list = notificationRepository.findAll();
            assertThat(list).hasSize(1);
            assertThat(list.get(0).getType()).isEqualTo(NotificationType.COMMENT);

            then(notificationAspect).should(times(1)).addCommentNotification(any(CommentResponseDto.class));
            then(notificationEventListener).should(times(1)).eventListener(any(NotificationEvent.class));
        }
    }

    @Transactional
    @Test
    @DisplayName("팔로우 시 알림 생성")
    void testAddFollowNotification() throws Exception {

        memberRepository.save(member1);
        memberRepository.save(member2);


        member1.verify();
        member2.verify();
        memberRepository.saveAllAndFlush(List.of(member1, member2));

        UserDetails user = new CustomUserDetail(member1);

        int threads = 1;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        executor.submit(() -> relationService.requestFollow(user, member2.getId()));

        if (executor.awaitTermination(1, TimeUnit.SECONDS)) {
            executor.shutdown();
            List<Notification> list = notificationRepository.findAll();
            assertThat(list).hasSize(1);
            assertThat(list.get(0).getType()).isEqualTo(NotificationType.FOLLOW);

            then(notificationAspect).should(times(1)).addFollowNotification(any(FollowAddResponse.class));
            then(notificationEventListener).should(times(1)).eventListener(any(NotificationEvent.class));
        }
    }

}
