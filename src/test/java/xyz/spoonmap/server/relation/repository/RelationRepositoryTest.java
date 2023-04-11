package xyz.spoonmap.server.relation.repository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import xyz.spoonmap.server.config.jpa.QueryDslConfig;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.member.repository.MemberRepository;
import xyz.spoonmap.server.relation.entity.Relation;

@DataJpaTest
@Import(QueryDslConfig.class)
class RelationRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    RelationRepository relationRepository;

    Member member1;
    Member member2;

    @BeforeEach
    void setUp() {
        member1 =
            new Member("김철수", "email1@email.com", "passwordpasswordpasswordpasswordpasswordpasswordpassword", "철수",
                null);

        member2 =
            new Member("김영희", "email2@email.com", "passwordpasswordpasswordpasswordpasswordpasswordpasswordpass", "영희",
                null);
    }

    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
        relationRepository.deleteAll();
    }

    @Test
    @DisplayName("팔로워 조회")
    void testFindFollowers() {
        Member member = memberRepository.save(member1);
        memberRepository.save(member2);

        Relation relation = new Relation(member1, member2);
        relationRepository.save(relation);

        List<Member> followers = relationRepository.findFollowers(member.getId());

        assertThat(followers).hasSize(1);
    }

    @Test
    @DisplayName("팔로우 조회")
    void testFindFollows() {
        Member followSender = memberRepository.save(member1);
        Member followReceiver = memberRepository.save(member2);

        Relation relation = new Relation(followSender, followReceiver);
        relationRepository.save(relation);

        List<Member> followers = relationRepository.findFollows(followReceiver.getId());

        assertThat(followers).hasSize(1);
    }

    @Test
    @DisplayName("나의 팔로우 요청 조회 (내가 다른 사람을)")
    void findMyFollowRequest() {
        Member followSender = memberRepository.save(member1);
        Member followReceiver = memberRepository.save(member2);

        Relation relation = new Relation(followSender, followReceiver);
        relationRepository.save(relation);

        List<Member> myFollowRequest = relationRepository.findMyFollowRequest(followSender.getId());
        assertThat(myFollowRequest).hasSize(1);
    }

    @Test
    @DisplayName("나의 팔로워 요청 조회 (다른 사람이 나를)")
    void findMyFollowerRequest() {
        Member followSender = memberRepository.save(member1);
        Member followReceiver = memberRepository.save(member2);

        Relation relation = new Relation(followSender, followReceiver);
        relationRepository.save(relation);

        List<Member> followers = relationRepository.findMyFollowerRequest(followReceiver.getId());
        assertThat(followers).hasSize(1);
    }
}