package xyz.spoonmap.server.member.controller.v1;

import static org.springframework.http.HttpStatus.OK;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.spoonmap.server.dto.response.Response;
import xyz.spoonmap.server.member.dto.request.PasswordUpdateRequest;
import xyz.spoonmap.server.member.dto.response.EmailResponse;
import xyz.spoonmap.server.member.dto.response.MemberResponse;
import xyz.spoonmap.server.member.service.MemberProfileService;

@RestController
@RequestMapping("/v1/members/profile")
@RequiredArgsConstructor
public class MemberProfileControllerV1 {

    private final MemberProfileService memberProfileService;

    @GetMapping("/{nickname}")
    public ResponseEntity<Response<MemberResponse>> retrieveByNickname(@PathVariable final String nickname) {
        MemberResponse memberResponse = memberProfileService.retrieveByNickname(nickname);
        return ResponseEntity.status(OK)
                             .body(Response.of(OK.value(), memberResponse));
    }

    @PatchMapping
    public ResponseEntity<Response<EmailResponse>> updatePassword(UserDetails userDetails, @RequestBody @Valid PasswordUpdateRequest request) {
        EmailResponse response = memberProfileService.updatePassword(userDetails, request.password());
        return ResponseEntity.status(OK)
                             .body(Response.of(OK.value(), response));
    }
}
