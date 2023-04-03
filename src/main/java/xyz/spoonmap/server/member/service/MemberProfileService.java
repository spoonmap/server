package xyz.spoonmap.server.member.service;

import org.springframework.security.core.userdetails.UserDetails;
import xyz.spoonmap.server.member.dto.response.EmailResponse;
import xyz.spoonmap.server.member.dto.response.MemberResponse;

public interface MemberProfileService {

    MemberResponse retrieveByNickname(String nickname);

    EmailResponse updatePassword(UserDetails userDetails, String newPassword);

    MemberResponse updateNickname(UserDetails userDetails, String newNickname);

}
