package xyz.spoonmap.server.member.service;

import xyz.spoonmap.server.member.dto.request.SignupRequest;
import xyz.spoonmap.server.member.dto.response.SignupResponse;
import xyz.spoonmap.server.member.dto.response.WithdrawResponse;

public interface MemberService {

    SignupResponse signUp(SignupRequest signupRequest);

    SignupResponse verify(Long code);

    WithdrawResponse withdraw(String email);

}
