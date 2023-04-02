package xyz.spoonmap.server.member.service.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.spoonmap.server.exception.member.MemberNotFoundException;
import xyz.spoonmap.server.member.dto.response.MemberResponse;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.member.repository.MemberProfileRepository;
import xyz.spoonmap.server.member.service.MemberProfileService;

@Service
@RequiredArgsConstructor
public class MemberProfileServiceV1 implements MemberProfileService {

    private final MemberProfileRepository memberProfileRepository;

    @Override
    public MemberResponse retrieveByNickname(String nickname) {
        Member member = memberProfileRepository.findByNickname(nickname).orElseThrow(MemberNotFoundException::new);
        return new MemberResponse(member.getId(), member.getName(), member.getNickname(), member.getAvatar());
    }

    @Override
    public void updatePassword() {

    }

}
