package xyz.spoonmap.server.member.service.v1;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;
import xyz.spoonmap.server.exception.member.MemberWithdrawException;
import xyz.spoonmap.server.member.dto.request.SignupRequest;
import xyz.spoonmap.server.member.dto.response.EmailResponse;
import xyz.spoonmap.server.member.dto.response.SignupResponse;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.member.enums.VerifyStatus;
import xyz.spoonmap.server.member.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
@Transactional
class MemberServiceV1Test {

    @InjectMocks
    MemberServiceV1 memberService;

    @Mock
    MemberRepository memberRepository;

    Long id = 1L;
    String email = "email@email.com";
    String password = "passw0rd";
    String nickname = "Nick";
    String name = "홍길동";

    @DisplayName("회원가입")
    @Test
    void signUp() {

        SignupRequest request = new SignupRequest(email, password, nickname, name);

        Member member = mock(Member.class);
        given(member.getId()).willReturn(1L);
        given(member.getEmail()).willReturn(email);

        given(memberRepository.save(any(Member.class))).willReturn(member);

        SignupResponse response = memberService.signUp(request);

        then(memberRepository).should(times(1)).save(any(Member.class));
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.email()).isEqualTo(email);
    }

    @DisplayName("회원가입 후 이메일 인증")
    @Test
    void verify() {
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
        assertThat(member.getVerifyStatus()).isEqualTo(VerifyStatus.VERIFIED);
    }


    @DisplayName("회원탈퇴")
    @Test
    void testWithdraw() {
        Member member = mock(Member.class);

        given(member.getEmail()).willReturn(email);
        given(member.getDeletedAt()).willReturn(null);
        willDoNothing().given(member).withdraw();

        given(memberRepository.findByEmail(email)).willReturn(Optional.of(member));

        EmailResponse emailResponse = memberService.withdraw(email);

        assertThat(emailResponse.email()).isEqualTo(email);

        then(member).should(times(1)).withdraw();
        then(member).should(times(1)).getEmail();
        then(member).should(times(1)).getDeletedAt();
        then(memberRepository).should(times(1)).findByEmail(email);
    }

    @DisplayName("이미 회원탈퇴한 회원 탈퇴 시도")
    @Test
    void testWithdrawFail() {
        Member member = mock(Member.class);

        given(member.getDeletedAt()).willReturn(LocalDateTime.now());
        given(memberRepository.findByEmail(email)).willReturn(Optional.of(member));

        assertThatThrownBy(() -> memberService.withdraw(email))
            .isInstanceOf(MemberWithdrawException.class);
    }

    @DisplayName("이메일 조회")
    @Test
    void testRetrieveMemberByEmail() {
        Member member = mock(Member.class);

        given(memberRepository.findByEmail(email)).willReturn(Optional.of(member));

        EmailResponse response = memberService.retrieveMemberByEmail(email);

        assertThat(response.email()).isEqualTo(email);

        then(memberRepository).should(times(1)).findByEmail(email);
    }

}
