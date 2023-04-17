package xyz.spoonmap.server.member.service.v1;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import xyz.spoonmap.server.authentication.CustomUserDetail;
import xyz.spoonmap.server.exception.domain.member.DuplicateException;
import xyz.spoonmap.server.exception.domain.member.MemberNotFoundException;
import xyz.spoonmap.server.member.dto.response.EmailResponse;
import xyz.spoonmap.server.member.dto.response.MemberResponse;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.member.enums.DuplicationType;
import xyz.spoonmap.server.member.repository.MemberProfileRepository;
import xyz.spoonmap.server.member.service.MemberProfileService;
import xyz.spoonmap.server.photo.adapter.S3Adapter;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberProfileServiceV1 implements MemberProfileService {

    private final MemberProfileRepository memberProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Adapter s3Adapter;

    @Override
    public MemberResponse retrieveByNickname(final String nickname) {
        Member member = memberProfileRepository.findByNickname(nickname).orElseThrow(MemberNotFoundException::new);
        return new MemberResponse(member.getId(), member.getName(), member.getNickname(), member.getAvatar());
    }

    @Transactional
    @Override
    public EmailResponse updatePassword(final String newPassword, final UserDetails userDetails) {
        Member member = ((CustomUserDetail) userDetails).getMember();
        member.updatePassword(passwordEncoder.encode(newPassword));

        memberProfileRepository.save(member);

        return new EmailResponse(member.getEmail());
    }

    @Transactional
    @Override
    public MemberResponse updateNickname(final String newNickname, final UserDetails userDetails) {
        Member member = ((CustomUserDetail) userDetails).getMember();
        member.updateNickname(newNickname);

        try {
            Member updatedMember = memberProfileRepository.save(member);

            return new MemberResponse(updatedMember.getId(), updatedMember.getName(), updatedMember.getNickname(),
                updatedMember.getAvatar());

        } catch (DataIntegrityViolationException e) {
            throw new DuplicateException(DuplicationType.NICKNAME);
        }
    }

    @Transactional
    @Override
    public MemberResponse updateProfileImage(final MultipartFile image, final UserDetails userDetails)
        throws IOException {
        Member member = ((CustomUserDetail) userDetails).getMember();
        String profileImageUrl = s3Adapter.upload(image);

        member.updateProfileImage(profileImageUrl);
        Member updatedMember = memberProfileRepository.save(member);

        return new MemberResponse(updatedMember.getId(), updatedMember.getName(), updatedMember.getNickname(),
            updatedMember.getAvatar());
    }

}
