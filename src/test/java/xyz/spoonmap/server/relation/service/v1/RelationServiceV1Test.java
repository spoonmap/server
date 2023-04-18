package xyz.spoonmap.server.relation.service.v1;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import xyz.spoonmap.server.authentication.CustomUserDetail;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.member.repository.MemberRepository;
import xyz.spoonmap.server.relation.dto.response.FollowAddResponse;
import xyz.spoonmap.server.relation.dto.response.FollowResponse;
import xyz.spoonmap.server.relation.dto.response.FollowerResponse;
import xyz.spoonmap.server.relation.entity.Relation;
import xyz.spoonmap.server.relation.repository.RelationRepository;

@ExtendWith(MockitoExtension.class)
class RelationServiceV1Test {

    @InjectMocks
    RelationServiceV1 relationServiceV1;

    @Mock
    RelationRepository relationRepository;
    @Mock
    MemberRepository memberRepository;

    CustomUserDetail userDetails;
    Member member;
    Long id;

    @BeforeEach
    void setUp() {
        userDetails = mock(CustomUserDetail.class);
        member = mock(Member.class);
        id = 1L;

        given(userDetails.getMember()).willReturn(member);
        given(member.getId()).willReturn(id);
    }

    @Test
    @DisplayName("팔로우 조회")
    void testRetrieveFollows() {
        given(relationRepository.findFollows(id)).willReturn(List.of());

        FollowResponse followResponse = relationServiceV1.retrieveFollows(userDetails);

        assertThat(followResponse.id()).isEqualTo(id);
        assertThat(followResponse.follows()).isEmpty();

        then(relationRepository).should(times(1)).findFollows(id);
    }

    @Test
    @DisplayName("팔로워 조회")
    void testRetrieveFollowers() {
        given(relationRepository.findFollowers(id)).willReturn(List.of());

        FollowerResponse followerResponse = relationServiceV1.retrieveFollowers(userDetails);

        assertThat(followerResponse.id()).isEqualTo(id);
        assertThat(followerResponse.followers()).isEmpty();

        then(relationRepository).should(times(1)).findFollowers(id);
    }

    @Test
    @DisplayName("팔로우 요청")
    void testRequestFollow() {
        Long receiverId = 2L;
        Member receiver = mock(Member.class);

        given(memberRepository.findById(receiverId)).willReturn(Optional.of(receiver));
        given(relationRepository.save(any(Relation.class))).willReturn(mock(Relation.class));

        FollowAddResponse followAddResponse = relationServiceV1.requestFollow(userDetails, receiverId);

        assertThat(followAddResponse.senderId()).isEqualTo(member.getId());
        assertThat(followAddResponse.receiverId()).isEqualTo(receiverId);

        then(memberRepository).should(times(1)).findById(receiverId);
        then(relationRepository).should(times(1)).save(any(Relation.class));
    }

    @Test
    @DisplayName("팔로우 수락")
    void testAcceptFollow() {
        Relation relation = mock(Relation.class);
        Long senderId = 2L;

        willDoNothing().given(relation).accept();
        given(relationRepository.findById(any(Relation.Pk.class))).willReturn(Optional.of(relation));

        FollowAddResponse followAddResponse = relationServiceV1.acceptFollow(senderId, userDetails);

        assertThat(followAddResponse.senderId()).isEqualTo(senderId);
        assertThat(followAddResponse.receiverId()).isEqualTo(id);

        then(relationRepository).should(times(1)).findById(any(Relation.Pk.class));
    }

    @Test
    @DisplayName("팔로우 요청 목록 조회")
    void testRetrieveFollowRequest() {
        given(relationRepository.findMyFollowRequest(id)).willReturn(List.of());

        FollowResponse followResponse = relationServiceV1.retrieveFollowRequest(userDetails);

        assertThat(followResponse.id()).isEqualTo(id);
        assertThat(followResponse.follows()).isEmpty();

        then(relationRepository).should(times(1)).findMyFollowRequest(id);
    }

    @Test
    @DisplayName("팔로워 요청 목록 조회")
    void testRetrieveFollowerRequest() {
        given(relationRepository.findMyFollowerRequest(id)).willReturn(List.of());

        FollowerResponse followerResponse = relationServiceV1.retrieveFollowerRequest(userDetails);

        assertThat(followerResponse.id()).isEqualTo(id);
        assertThat(followerResponse.followers()).isEmpty();

        then(relationRepository).should(times(1)).findMyFollowerRequest(id);
    }

    @Test
    @DisplayName("팔로우 거절")
    void testRejectFollow() {

        Relation relation = mock(Relation.class);
        Long senderId = 2L;

        given(relationRepository.findById(any(Relation.Pk.class))).willReturn(Optional.of(relation));
        willDoNothing().given(relation).reject();

        FollowAddResponse followAddResponse = relationServiceV1.rejectFollow(senderId, userDetails);

        assertThat(followAddResponse.senderId()).isEqualTo(senderId);
        assertThat(followAddResponse.receiverId()).isEqualTo(id);

        then(relation).should(times(1)).reject();
    }
}