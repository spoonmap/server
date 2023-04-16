package xyz.spoonmap.server.member.service.v1;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;

import java.io.IOException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import xyz.spoonmap.server.authentication.CustomUserDetail;
import xyz.spoonmap.server.member.dto.response.EmailResponse;
import xyz.spoonmap.server.member.dto.response.MemberResponse;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.member.repository.MemberProfileRepository;
import xyz.spoonmap.server.photo.adapter.S3Adapter;
import xyz.spoonmap.server.util.RandomEntityGenerator;

@ExtendWith(MockitoExtension.class)
class MemberProfileServiceV1Test {

    @InjectMocks
    MemberProfileServiceV1 memberProfileService;

    @Mock
    MemberProfileRepository memberProfileRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    S3Adapter s3Adapter;

    Member member;
    CustomUserDetail userDetail;

    @BeforeEach
    void setUp() {
        member = spy(RandomEntityGenerator.createWithId(Member.class));
        userDetail = new CustomUserDetail(member);
    }

    @Test
    @DisplayName("닉네임으로 회원조회")
    void testRetrieveByNickname() {
        String nickname = member.getNickname();

        given(memberProfileRepository.findByNickname(nickname)).willReturn(Optional.of(member));

        MemberResponse memberResponse = memberProfileService.retrieveByNickname(nickname);

        assertThat(memberResponse.id()).isEqualTo(member.getId());
        assertThat(memberResponse.nickname()).isEqualTo(member.getNickname());

        then(memberProfileRepository).should(times(1)).findByNickname(nickname);
    }

    @Test
    @DisplayName("비밀번호 변경")
    void testUpdatePassword() {
        String newPassword = "password";
        String encodedPassword = "encodedPassword";

        given(passwordEncoder.encode(newPassword)).willReturn(encodedPassword);
        given(memberProfileRepository.save(member)).willReturn(member);

        EmailResponse response = memberProfileService.updatePassword(newPassword, userDetail);

        assertThat(response.email()).isEqualTo(member.getEmail());

        then(passwordEncoder).should(times(1)).encode(newPassword);
        then(memberProfileRepository).should(times(1)).save(member);
        then(member).should(times(1)).updatePassword(encodedPassword);
    }

    @Test
    @DisplayName("닉네임 변경")
    void testUpdateNickname() {
        String newNickname = "nickname";

        given(memberProfileRepository.save(member)).willReturn(member);

        MemberResponse memberResponse = memberProfileService.updateNickname(newNickname, userDetail);

        assertThat(memberResponse.id()).isEqualTo(member.getId());
        assertThat(memberResponse.nickname()).isEqualTo(member.getNickname());

        then(member).should(times(1)).updateNickname(newNickname);
        then(memberProfileRepository).should(times(1)).save(member);
    }

    @Test
    @DisplayName("프로필 이미지 변경")
    void testUpdateProfileImage() throws IOException {
        MultipartFile image = mock(MultipartFile.class);
        String returnUrl = "tmp";

        given(s3Adapter.upload(image)).willReturn(returnUrl);
        given(memberProfileRepository.save(member)).willReturn(member);

        MemberResponse memberResponse = memberProfileService.updateProfileImage(image, userDetail);

        assertThat(memberResponse.id()).isEqualTo(member.getId());
        assertThat(memberResponse.profileUrl()).isEqualTo(member.getAvatar());

        then(member).should(times(1)).updateProfileImage(returnUrl);
        then(memberProfileRepository).should(times(1)).save(member);
    }

}