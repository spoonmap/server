package xyz.spoonmap.server.member.service.v1;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.spoonmap.server.exception.domain.member.MemberNotFoundException;
import xyz.spoonmap.server.exception.domain.member.MemberWithdrawException;
import xyz.spoonmap.server.member.dto.request.SignupRequest;
import xyz.spoonmap.server.member.dto.response.EmailResponse;
import xyz.spoonmap.server.member.dto.response.MemberRetrieveResponse;
import xyz.spoonmap.server.member.dto.response.PasswordUpdateResponse;
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
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public SignupResponse signUp(final SignupRequest signupRequest) {

        Member member = Member.builder()
                              .email(signupRequest.email())
                              .password(passwordEncoder.encode(signupRequest.password()))
                              .nickname(signupRequest.nickname())
                              .name(signupRequest.name())
                              .build();

        log.info("========================");
        log.info(member.getPassword());
        log.info("{}",member.getPassword().length());
        log.info("========================");

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

    @Transactional
    @Override
    public EmailResponse withdraw(final String email) {
        Member member = memberRepository.findByEmail(email)
                                        .orElseThrow(MemberNotFoundException::new);

        if (Objects.nonNull(member.getDeletedAt())) {
            throw new MemberWithdrawException();
        }

        member.withdraw();
        return new EmailResponse(member.getEmail());
    }

    @Override
    public EmailResponse retrieveMemberByEmail(final String email) {
        Member member = memberRepository.findByEmail(email)
                                        .orElseThrow(MemberNotFoundException::new);
        return new EmailResponse(member.getEmail());
    }

    @Transactional
    @Override
    public PasswordUpdateResponse findPassword(final String email) {
        Member member = memberRepository.findByEmail(email)
                                        .orElseThrow(MemberNotFoundException::new);

        String updatedPassword = generateRawPassword();
        String encodedPassword = passwordEncoder.encode(updatedPassword);
        member.updatePassword(encodedPassword);

        return new PasswordUpdateResponse(email, updatedPassword);
    }

    @Transactional
    @Override
    public MemberRetrieveResponse retrieveMembersByNickname(final String nickname) {
        return new MemberRetrieveResponse(memberRepository.findMembersByNickname(nickname));
    }

}
