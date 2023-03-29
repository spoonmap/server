package xyz.spoonmap.server.member.controller.v1;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.spoonmap.server.dto.response.Response;
import xyz.spoonmap.server.member.dto.request.SignupRequest;
import xyz.spoonmap.server.member.dto.response.PasswordUpdateResponse;
import xyz.spoonmap.server.member.dto.response.SignupResponse;
import xyz.spoonmap.server.member.dto.response.EmailResponse;
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

    @DeleteMapping
    public ResponseEntity<Response<EmailResponse>> withdraw(@RequestParam @Email @Size(max = 500) String email) {
        EmailResponse response = memberService.withdraw(email);
        return ResponseEntity.status(NO_CONTENT)
                             .body(Response.of(NO_CONTENT.value(), response));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Response<EmailResponse>> retrieveMemberByEmail(@PathVariable @Email @Size(max = 500) String email) {
        EmailResponse response = memberService.retrieveMemberByEmail(email);
        return ResponseEntity.status(OK)
                             .body(Response.of(OK.value(), response));
    }

    @PatchMapping("/password")
    public ResponseEntity<Response<PasswordUpdateResponse>> findPassword(@RequestParam String email) {
        PasswordUpdateResponse response = memberService.findPassword(email);
        return ResponseEntity.status(OK)
                             .body(Response.of(OK.value(), response));
    }

}
