package xyz.spoonmap.server.member.service.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.spoonmap.server.member.dto.request.SignupRequest;
import xyz.spoonmap.server.member.dto.response.SignupResponse;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.member.repository.MemberRepository;
import xyz.spoonmap.server.member.service.MemberService;

@Service
@RequiredArgsConstructor
public class MemberServiceV1 implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public SignupResponse signUp(final SignupRequest signupRequest) {

        Member member = Member.builder()
                              .email(signupRequest.email())
                              .password(signupRequest.password())
                              .nickname(signupRequest.nickname())
                              .name(signupRequest.name())
                              .build();

        memberRepository.save(member);

        return new SignupResponse(member.getEmail());
    }

}
