package xyz.spoonmap.server.member.controller.v1;

import static org.springframework.http.HttpStatus.CREATED;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.spoonmap.server.dto.response.Response;
import xyz.spoonmap.server.member.dto.request.SignupRequest;
import xyz.spoonmap.server.member.dto.response.SignupResponse;
import xyz.spoonmap.server.member.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/members")
public class MemberControllerV1 {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<Response<SignupResponse>> signup(@RequestBody @Valid SignupRequest signupRequest) {

        SignupResponse signupResponse = memberService.signUp(signupRequest);
        return ResponseEntity.status(CREATED)
                             .body(Response.of(CREATED.value(), signupResponse));
    }

}
