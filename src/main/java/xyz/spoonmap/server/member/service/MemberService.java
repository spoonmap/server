package xyz.spoonmap.server.member.service;

import xyz.spoonmap.server.member.dto.request.SignupRequest;
import xyz.spoonmap.server.member.dto.response.MemberRetrieveResponse;
import xyz.spoonmap.server.member.dto.response.PasswordUpdateResponse;
import xyz.spoonmap.server.member.dto.response.SignupResponse;
import xyz.spoonmap.server.member.dto.response.EmailResponse;

public interface MemberService {

    SignupResponse signUp(SignupRequest signupRequest);

    SignupResponse verify(Long code);

    EmailResponse withdraw(String email);

    EmailResponse retrieveMemberByEmail(String email);

    PasswordUpdateResponse findPassword(String email);

    MemberRetrieveResponse retrieveMembersByNickname(String nickname);

}
