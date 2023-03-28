package xyz.spoonmap.server.member.service.v1;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;
import xyz.spoonmap.server.member.dto.request.SignupRequest;
import xyz.spoonmap.server.member.dto.response.SignupResponse;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.member.enums.Status;
import xyz.spoonmap.server.member.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
@Transactional
class MemberServiceV1Test {

    @InjectMocks
    MemberServiceV1 memberService;

    @Mock
    MemberRepository memberRepository;

    @DisplayName("회원가입")
    @Test
    void signUp() {
        String email = "email@email.com";
        String password = "passw0rd";
        String nickname = "Nick";
        String name = "홍길동";
        SignupRequest signupRequest = new SignupRequest(email, password, nickname, name);

        Member member = mock(Member.class);
        given(member.getId()).willReturn(1L);
        given(member.getEmail()).willReturn(email);

        given(memberRepository.save(any(Member.class))).willReturn(member);

        SignupResponse response = memberService.signUp(signupRequest);

        then(memberRepository).should(times(1)).save(any(Member.class));
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.email()).isEqualTo(email);
    }

    @DisplayName("회원가입 후 이메일 인증")
    @Test
    void verify() {
        Long id = 1L;
        String email = "email@email.com";
        Member member = spy(Member.builder()
                                  .email(email)
                                  .password("passw0rd")
                                  .nickname("nick")
                                  .name("홍길동")
                                  .build());

        ReflectionTestUtils.setField(member, "id", id);
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

        SignupResponse verify = memberService.verify(1L);

        then(memberRepository).should(times(1)).findById(id);
        then(member).should(times(1)).verify();

        assertThat(verify.id()).isEqualTo(id);
        assertThat(verify.email()).isEqualTo(email);
        assertThat(member.getVerifyStatus()).isEqualTo(Status.VERIFIED);
    }

}
