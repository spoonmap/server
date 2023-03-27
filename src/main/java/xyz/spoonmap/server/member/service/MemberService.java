package xyz.spoonmap.server.member.service;

import xyz.spoonmap.server.member.dto.request.SignupRequest;
import xyz.spoonmap.server.member.dto.response.SignupResponse;

public interface MemberService {

    SignupResponse signUp(final SignupRequest signupRequest);

}
