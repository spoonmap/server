package xyz.spoonmap.server.member.service.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.spoonmap.server.exception.member.MemberNotFoundException;
import xyz.spoonmap.server.member.dto.request.SignupRequest;
import xyz.spoonmap.server.member.dto.response.SignupResponse;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.member.repository.MemberRepository;
import xyz.spoonmap.server.member.service.MemberService;

@Slf4j
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MemberServiceV1 implements MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public SignupResponse signUp(final SignupRequest signupRequest) {

        Member member = Member.builder()
                              .email(signupRequest.email())
                              .password(signupRequest.password())
                              .nickname(signupRequest.nickname())
                              .name(signupRequest.name())
                              .build();

        Member savedMember = memberRepository.save(member);

        return new SignupResponse(savedMember.getId(), savedMember.getEmail());
    }

    @Transactional
    @Override
    public SignupResponse verify(final Long code) {
        Member member = memberRepository.findById(code).orElseThrow(MemberNotFoundException::new);
        member.verify();
        return new SignupResponse(member.getId(), member.getEmail());
    }

}
