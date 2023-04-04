package xyz.spoonmap.server.relation.service.v1;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.spoonmap.server.authentication.CustomUserDetail;
import xyz.spoonmap.server.exception.member.MemberNotFoundException;
import xyz.spoonmap.server.exception.relation.RelationNotFoundException;
import xyz.spoonmap.server.member.dto.response.MemberResponse;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.member.repository.MemberRepository;
import xyz.spoonmap.server.relation.dto.response.FollowAddResponse;
import xyz.spoonmap.server.relation.dto.response.FollowResponse;
import xyz.spoonmap.server.relation.dto.response.FollowerResponse;
import xyz.spoonmap.server.relation.entity.Relation;
import xyz.spoonmap.server.relation.repository.RelationRepository;
import xyz.spoonmap.server.relation.service.RelationService;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class RelationServiceV1 implements RelationService {

    private final RelationRepository relationRepository;
    private final MemberRepository memberRepository;

    @Override
    public FollowResponse retrieveFollows(UserDetails userDetails) {
        Member member = ((CustomUserDetail) userDetails).getMember();
        List<MemberResponse> memberResponses = relationRepository.findFollows(member.getId())
                                                                 .stream()
                                                                 .map(this::toDto)
                                                                 .toList();
        return new FollowResponse(member.getId(), memberResponses);
    }

    @Override
    public FollowerResponse retrieveFollowers(UserDetails userDetails) {
        Member member = ((CustomUserDetail) userDetails).getMember();
        List<MemberResponse> memberResponses = relationRepository.findFollowers(member.getId())
                                                                 .stream()
                                                                 .map(this::toDto)
                                                                 .toList();
        return new FollowerResponse(member.getId(), memberResponses);
    }

    @Transactional
    @Override
    public FollowAddResponse requestFollow(UserDetails userDetails, Long receiverId) {
        Member sender = ((CustomUserDetail) userDetails).getMember();
        Member receiver = memberRepository.findById(receiverId).orElseThrow(MemberNotFoundException::new);

        Relation relation = new Relation(sender, receiver);
        relationRepository.save(relation);

        return new FollowAddResponse(sender.getId(), receiverId);
    }

    private MemberResponse toDto(Member member) {
        return new MemberResponse(member.getId(), member.getName(), member.getNickname(), member.getAvatar());
    }

    @Transactional
    @Override
    public FollowAddResponse acceptFollow(Long senderId, UserDetails userDetails) {
        Member member = ((CustomUserDetail) userDetails).getMember();

        Relation relation = relationRepository.findById(new Relation.Pk(member.getId(), senderId))
                                              .orElseThrow(RelationNotFoundException::new);

        relation.accept();

        return new FollowAddResponse(senderId, member.getId());
    }

}
