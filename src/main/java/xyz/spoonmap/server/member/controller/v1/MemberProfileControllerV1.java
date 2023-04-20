package xyz.spoonmap.server.member.controller.v1;

import static org.springframework.http.HttpStatus.OK;

import java.io.IOException;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
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

    @GetMapping("/nickname/{nickname}")
    public ResponseEntity<Response<MemberResponse>> retrieveByNickname(@PathVariable final String nickname) {
        MemberResponse memberResponse = memberProfileService.retrieveByNickname(nickname);
        return ResponseEntity.status(OK)
                             .body(Response.of(OK.value(), memberResponse));
    }

    @PatchMapping("/password")
    public ResponseEntity<Response<EmailResponse>> updatePassword(@RequestBody @Valid PasswordUpdateRequest request,
                                                                  @AuthenticationPrincipal UserDetails userDetails) {
        EmailResponse response = memberProfileService.updatePassword(request.password(), userDetails);
        return ResponseEntity.status(OK)
                             .body(Response.of(OK.value(), response));
    }

    @PatchMapping("/nickname")
    public ResponseEntity<Response<MemberResponse>> updateNickname(@RequestParam @Size(min = 2, max = 10) String value,
                                                                   @AuthenticationPrincipal UserDetails userDetails) {
        MemberResponse memberResponse = memberProfileService.updateNickname(value, userDetails);
        return Response.success(OK, OK.value(), memberResponse);
    }

    @PatchMapping("/image")
    public ResponseEntity<Response<MemberResponse>> updateProfileImage(
        @RequestPart(value = "image") MultipartFile image,
        @AuthenticationPrincipal UserDetails userDetails) throws IOException {

        MemberResponse memberResponse = memberProfileService.updateProfileImage(image, userDetails);

        return Response.success(OK, OK.value(), memberResponse);
    }

}
