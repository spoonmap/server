package xyz.spoonmap.server.member.controller.v1;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import xyz.spoonmap.server.member.dto.request.SignupRequest;
import xyz.spoonmap.server.member.dto.response.SignupResponse;
import xyz.spoonmap.server.member.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/members")
public class MemberControllerV1 {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody @Valid SignupRequest signupRequest) {

        SignupResponse signupResponse = memberService.signUp(signupRequest);
        return ResponseEntity.status(CREATED)
                             .body(signupResponse);
    }

}
