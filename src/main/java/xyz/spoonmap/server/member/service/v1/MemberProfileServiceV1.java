package xyz.spoonmap.server.member.service.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.spoonmap.server.authentication.CustomUserDetail;
import xyz.spoonmap.server.exception.domain.member.MemberNotFoundException;
import xyz.spoonmap.server.member.dto.response.EmailResponse;
import xyz.spoonmap.server.member.dto.response.MemberResponse;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.member.repository.MemberProfileRepository;
import xyz.spoonmap.server.member.service.MemberProfileService;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MemberProfileServiceV1 implements MemberProfileService {

    private final MemberProfileRepository memberProfileRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public MemberResponse retrieveByNickname(final String nickname) {
        Member member = memberProfileRepository.findByNickname(nickname).orElseThrow(MemberNotFoundException::new);
        return new MemberResponse(member.getId(), member.getName(), member.getNickname(), member.getAvatar());
    }

    @Transactional
    @Override
    public EmailResponse updatePassword(final UserDetails userDetails, final String newPassword) {
        Member member = ((CustomUserDetail) userDetails).getMember();
        member.updatePassword(passwordEncoder.encode(newPassword));

        memberProfileRepository.save(member);

        return new EmailResponse(member.getEmail());
    }

    @Transactional
    @Override
    public MemberResponse updateNickname(final UserDetails userDetails, final String newNickname) {

        throw new MemberNotFoundException();
        // Member member = ((CustomUserDetail) userDetails).getMember();
        // member.updateNickname(newNickname);
        //
        // memberProfileRepository.save(member);

        // return new MemberResponse(member.getId(), member.getName(), member.getNickname(), member.getAvatar());
    }

}
