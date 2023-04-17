package xyz.spoonmap.server.member.service;

import java.io.IOException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
import xyz.spoonmap.server.member.dto.response.EmailResponse;
import xyz.spoonmap.server.member.dto.response.MemberResponse;

public interface MemberProfileService {

    MemberResponse retrieveByNickname(String nickname);

    EmailResponse updatePassword(String newPassword, UserDetails userDetails);

    MemberResponse updateNickname(String newNickname, UserDetails userDetails);

    MemberResponse updateProfileImage(MultipartFile image, UserDetails userDetails) throws IOException;

}
